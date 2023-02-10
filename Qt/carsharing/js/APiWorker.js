//Minden fontos API hívás vagy azzal kapcsolatos JSON parsolás

//GET&POST
    //Use to get(GET) from specific endpoint
    function get(endPoint,callBackFgv)
    {
        //console.log("getting data from: "+endPoint);
        var xmlhttp = new XMLHttpRequest();
        var url = "http://localhost:8080"+endPoint;
        xmlhttp.onreadystatechange=function()
        {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200)
            {
                //console.log("GET response text: "+xmlhttp.responseText)
                callBackFgv(xmlhttp.responseText);
            }
        }
        xmlhttp.open("GET", url, true);
        xmlhttp.send();
    }

    //Use to send(POST) to specific endpoint
    function post(endPoint, dataToSend, callBackFgv)
    {
        //console.log("POST");
        var xmlhttp = new XMLHttpRequest();
        var url = "http://localhost:8080"+endPoint;
        xmlhttp.open("POST", url, true);
        xmlhttp.setRequestHeader("Accept", "application/json");
        xmlhttp.setRequestHeader("Content-Type", "application/json");

        xmlhttp.onreadystatechange=function(){
            if (xmlhttp.readyState == 4)
            {
                //console.log("STATUS FOR POST: "+xmlhttp.status);
                if(xmlhttp.status == 200)
                {
                    var obj = JSON.parse(xmlhttp.responseText);
                    if(typeof callBackFgv !== "undefined")
                        callBackFgv(obj);
                }
                else if(xmlhttp.status == 400)
                {
                    callBackFgv(false);
                }
                else
                {
                    if(typeof callBackFgv !== "undefined")
                    {
                        callBackFgv();
                    }
                    else
                    {
                        //console.log("POST: ez a STATUS: "+xmlhttp.status);
                    }
                }
           }
           else
           {
                if(typeof callBackFgv !== "undefined")
                {
                    callBackFgv();
                }

           }

        }

        var data = JSON.stringify(dataToSend);
        xmlhttp.send(data);
    }
//GET&POST

//CARS
    //Listába rakás
    function jsonParserForCars(json, listID)
    {
        var obj = JSON.parse(json);
        obj.forEach(
            function(entry)
            {
                var data = entry;
                listID.model.append( {carData: data });
            }
        );
    }
    //Adatok visszaadása
    function jsonParserForCars_Value(json)
    {
        var ertekek = [];
        var obj = JSON.parse(json);
        obj.forEach(
            function(entry)
            {
                var data = entry;
                ertekek.push(data);
            }
        );

        return ertekek;
    }
    //Comboboxnak, csak szöveg
    function jsonParserForCarsWithFilter(json, listID)
    {
        var obj = JSON.parse(json);
        obj.forEach(
            function(entry)
            {
                var data = entry;
                console.log("RENTER IS: "+data.renter);
                if(data.renter === null)
                {
                    console.log("ez nem foglalt");
                    listID.model.append( {carData: data });
                }

                //else
                    //console.log(data.licensePlate+"mar egy foglalt kocsi by: "+data.renter.name)
            }
        );
    }
//CARS

//SITES
    function jsonParserForSites(json, listID)
    {
        var obj = JSON.parse(json);
        obj.forEach(
            function(entry)
            {
                var data = entry;
                listID.model.append( {siteData: data });
            }
        );
    }

     //Comboboxnak, csak szöveg
    function jsonParserForSites_Name(json)
    {
        var ertekek = [];
        var obj = JSON.parse(json);
        obj.forEach(
            function(entry)
            {
                var data = entry;
                ertekek.push(data.address);
            }
        );
        return ertekek;
    }

    function jsonParserForSites_Value(json)
    {
        var ertekek = [];
        var obj = JSON.parse(json);
        obj.forEach(
            function(entry)
            {
                var data = entry;
                ertekek.push(data);
            }
        );
        return ertekek;
    }
//SITES

//CATEGORIES
    //Kategoria ComboBox lista szovegnek - property ertke
    function jsonParserForCategoriesName(json)
    {
        var ertekek = [];
        var obj = JSON.parse(json);
        obj.forEach(
            function(entry)
            {
                var data = entry;
                ertekek.push(data.name);
                console.log("Category: " + entry.name);
            }
        );
        return ertekek;
    }
    //Kategoria ComboBox ertek(object) - property ertek
    function jsonParserForCategories(json)
    {
        var ertekek = [];
        var obj = JSON.parse(json);
        obj.forEach(
            function(entry)
            {
                var data = entry;
                ertekek.push(data);
                console.log("Category: " + entry.name);
            }
        );
        return ertekek;
    }
    //Kategoria
    function jsonParserForCategoriesList(json, listID)
    {
        var obj = JSON.parse(json);
        obj.forEach(
            function(entry)
            {
                var data = entry;
                listID.model.append( {categoryData: data });
            }
        );
    }
//CATEGORIES

//RENTALS
    function jsonParserForRentals(json, listID)
    {
        var obj = JSON.parse(json);
        obj.forEach(
            function(entry)
            {
                var data = entry;
                listID.model.append( {rentalData: data });
            }
        );
    }
//RENTALS
