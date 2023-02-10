package banger.service;

import banger.dto.LoginDTO;
import banger.dto.UserDTO;
import banger.model.*;
import banger.repository.RentalRepository;
import banger.repository.UserRepository;
import banger.service.transformer.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Value("${mailsending.on}")
    private boolean mailSending;

    @Value("${admin.password}")
    private String name;

    public static String ADMIN_KEY;

    @Value("${admin.password}")
    public void setAdminKey(String ak){
        UserService.ADMIN_KEY = ak;
    }

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    UserRepository repository;

    @Autowired
    RentalRepository rentalRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    Transformer transformer;

    @Autowired
    AuthenticationManager authenticationManager;

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    public User loginUser(LoginDTO login){
        logger.debug("login user: " + login.getUsername());
        Optional<User> u = repository.findByUsername(login.getUsername());
        if(u.isEmpty()) {
            logger.error("user does not exist");
            return null;
        }
        logger.info("user found");
        Authentication auth;
        try {
            auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
        } catch(AuthenticationException e){
            logger.error("authentication error");
            return null;
        }
        logger.info("new autentication created");
        SecurityContextHolder.getContext().setAuthentication(auth);
        logger.info("authentication set");
        User user = u.get();
        logger.debug(user.toString());
        return user;
    }

    public ResponseEntity<User> registerUser(UserDTO user){
        logger.debug("register user " + user.getUsername());
        if(repository.existsByUsername(user.getUsername()) || repository.existsByEmail(user.getEmail())) {
            logger.error("user with this email/username already not exist");
            return ResponseEntity.badRequest().build();
        }
        logger.info("user found");
        User u = transformer.transform(user);
        logger.info("user transformed");
        u.setPassword(passwordEncoder.encode(user.getPassword()));
        if(mailSending){
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@banger.com");
            message.setTo(u.getEmail());
            message.setSubject("Confirm your registration @ Banger Car-Sharing");
            repository.save(u);
            try {
                message.setText("http://"+ InetAddress.getLocalHost().getHostAddress()+":8080/api/users/confirm/"+u.getId());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            mailSender.send(message);
        }
        else{
            u.setEnabled(true);
            repository.save(u);
            logger.info("user saved");
        }
        ResponseEntity<User> responseEntity = ResponseEntity.ok(u);
        logger.debug(responseEntity.toString());
        return responseEntity;
    }

    public User update(String userId, UserDTO update){
        logger.info("update: " + userId + " " + update);
        Optional<User> oUser = repository.findById(userId);
        if(oUser.isEmpty()) {
            logger.error("user does not exist");
            return null;
        }
        User user = oUser.get();
        user = transformer.updateUser(user, update);
        logger.debug("user transformed");
        repository.save(user);
        logger.info("user updated successfully");
        return user;
    }

    public User find(String userID){
        logger.info("find user: " + userID);
        Optional<User> oUser = repository.findById(userID);
        if(oUser.isEmpty()){
            logger.error("user does not exist");
            return null;
        }
        logger.info("user found");
        return oUser.get();
    }

    public List<User> findAll() {
        logger.debug("find all");
        List<User> users = repository.findAll();
        logger.debug(users.toString());
        return users;
    }

    public String delete(String id){
        logger.debug("delete by id: " + id);
        if(!repository.existsById(id)){
            logger.error("user does not exist");
            return null;
        }
        logger.info("user found");
        repository.deleteById(id);
        logger.info("user deleted");
        return "Sikeres felhasználó törlés!";
    }

    public void notifySiteDeletion(Site s, Site newSite, List<Car> movedCars) {
        logger.debug("notify site deletion by id" + s);
        if(!mailSending) {
            logger.debug("mail sending turned off");
            return;
        }
        List<User> users = repository.findByAdminIsFalse();
        logger.info("users found");
        String emails = users.stream().filter(u -> !u.isAdmin()).map(User::getEmail).collect(Collectors.joining(","));
        JavaMailSender javaMailSender = new JavaMailSenderImpl();
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom("noreply@banger.com");
            InternetAddress[] recipients = InternetAddress.parse(emails);
            message.addRecipients(MimeMessage.RecipientType.BCC, recipients);
            message.setSubject("We're closing a site :(");
            StringBuilder msg = new StringBuilder();
            msg.append("<htm lang=\"hu\"><header><meta charset=\"utf-8\"></header><body><h1>We're sorry to inform you, that we're closing the following site:</h1>")
               .append("<ul>")
                    .append("<li>").append(s.getAddress()).append("</li>")
                    .append("<li>").append(s.getEmail()).append("</li>")
                    .append("<li>").append(s.getPhone()).append("</li>")
               .append("</ul>");
            if(!movedCars.isEmpty()) {
                msg.append("<p>The following cars have been moved to our site at "+newSite.getAddress()+", make sure to check their availability before renting!</p>");
                msg.append("<ul>");
                for (Car c : movedCars) {
                    msg.append("<li>").append(c.getManufacturer() + " " + c.getModel() + " [" + c.getLicensePlate() + "]").append("</li>");
                }
                msg.append("</ul>");
            }
            msg.append("</body>");
            message.setContent(msg.toString(), "text/html");
            mailSender.send(message);
            logger.info("message sent");
        } catch(MailException e){
            logger.error("email address formatting problem");
        } catch(MessagingException e){
            logger.error("couldn't send message");
        }
    }
    @Scheduled(cron = "0 0 10 * * 1") //minden hetfo 10:00:00-kor
    public void sendWeeklyData(){
        if(!mailSending) return;
        List<User> users = repository.findByAdminIsFalse();
        for(User u : users){
            StringBuilder content = new StringBuilder();
            String tableCss = "<html><head><style>\n" +
                    "table, th, td {\n" +
                    "  border: 1px solid black;\n" +
                    "  border-collapse: collapse;\n" +
                    "  text-align:center;\n" +
                    "}"+
                    "</style></head><body>\n";
            content.append(tableCss);
            content.append("Dear ").append(u.getName()).append("!<br/>");
            LocalDateTime weekStart = LocalDateTime.now().minusDays(7).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime weekEnd = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0);
            List<Rental> weeklyRentals = rentalRepository.findByUserAndFromAfterAndToBefore(u, weekStart, weekEnd);
            if(weeklyRentals.isEmpty()){
                content.append("We see you haven't rented a car the last week!<br/>")
                       .append("We'd like to give you a coupon for your next renting, use it to get 25% off the first 4 hours!<br/>");
                content.append("Coupon code: <b>BANGERCPN</b><br/>");
            } else {
                double sumPrice = 0.0;

                String table = "<table>\n<tr><th>Car</th><th>Category[PPH]</th><th>Start</th><th>End</th><th>Price</th></tr>\n%s</table><br/>";
                String tableRow = "<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%.0f Ft</td></tr>\n";
                StringBuilder tableContent = new StringBuilder();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm");
                for(Rental r : weeklyRentals){
                    Car car = r.getCar();
                    Category category = car.getCategory();
                    double pph = category.getPricePerHour();
                    LocalDateTime start = r.getFrom();
                    LocalDateTime end = r.getTo();
                    Duration dur = Duration.between(start, end);
                    long hours = (long) Math.ceil(dur.toSeconds()/3600.0);
                    double price = pph * hours;
                    sumPrice += price;
                    tableContent.append(String.format(tableRow, car.getLicensePlate(), category.getName()+"["+pph+"]", start.format(dtf), end.format(dtf), price));
                }
                content.append(String.format("Summary: You have spent %.0f Ft on %d rentals this week. Find the details below:<br/>", sumPrice, weeklyRentals.size()));
                content.append(String.format(table, tableContent));
            }
            content.append("Yours sincerelly,<br/>Team Banger</body></html>");
            MimeMessage message = new JavaMailSenderImpl().createMimeMessage();
            try{
                message.setFrom("noreply@banger.com");
                InternetAddress[] recipients = InternetAddress.parse(u.getEmail());
                message.addRecipients(MimeMessage.RecipientType.TO, recipients);
                message.setSubject("Your weekly Banger report");
                message.setContent(content.toString(), "text/html");
                mailSender.send(message);
            } catch(MailException e){
                logger.error("mail does not exist");
            } catch(MessagingException e){
                logger.error("couldn't send message");
            }
        }
    }
}
