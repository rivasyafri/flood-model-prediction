var map;
var geom_getter=[];
var object=[];

// map intialitation and function
function initMap(){
    map = new google.maps.Map(document.getElementById('map-visual'), {
        center: {lat: -7.4143195, lng: 111.0043654},
        zoom: 12
    }); // edited the center and zoom by Riva
}
function focusMap(object){
    bounds= new google.maps.LatLngBounds();
    for(i = 0 ; i<object.length; i++){
        var point = new google.maps.LatLng(object[i]);
        bounds = bounds.extend(point);
    }
    map.fitBounds(bounds);
}
function clearGeomGetter(){
    while(geom_getter.length){
        geom_getter.pop().setMap(null);
    }
}
function clearMap(){
    clearGeomGetter();

    // clear another object
    while(object.length){
        var obj = object.pop();
        var linemarker_objs = obj.object;
        while(linemarker_objs.length){
            var single_object = linemarker_objs.pop();
            single_object.setMap(null);
        }
    }
    map.setCenter({lat:0,lng:0});
    map.setZoom(2);
}

// map attribute intialitation and function
function addLine(index,polyline_path,info_object){
    // generate line in map
    var path_line = new google.maps.Polyline({
        path:polyline_path,
        strokeColor:'blue',
        StrokeOvacity: 1.000000,
        map:map
    });
    var start_info = addMarker(polyline_path[0]);
    start_info.setIcon("http://icons.iconarchive.com/icons/icons-land/vista-flags/32/Solid-Color-White-Flag-3-icon.png");
    var finish_info = addMarker(polyline_path[polyline_path.length-1]);
    finish_info.setIcon("http://icons.iconarchive.com/icons/icons8/windows-8/32/Sports-Finish-Flag-icon.png");


    // checking and update object
    if(getObject(index) != null){
        var obj = getObject(index);
        while(obj.length){
            obj.pop().setMap(null);
        }
        object[checkIndexObject(index)].object=[start_info,finish_info,path_line];
    }else{
        var tuple_obje = {"index": index, "object":[start_info, finish_info, path_line]};
        object.push(tuple_obje);
    }
    focusMap(polyline_path);
    
    // generate info window string
    if(info_object!=null){
        x = '</th><th>';
        y= '</th></tr><tr><th>';
        string_info = "<table class=table table-bordered table-sm><tbody><tr><th>";
        string_info += "gid"+x+info_object.gid+y;
        string_info += 'start_time'+x+info_object.start_time+y;
        string_info += 'finish_time'+x+info_object.finish_time+y;
        string_info += 'total_time'+x+info_object.total_time+y;
        string_info += 'trajectory_length'+x+info_object.length+y;
        string_info +=  'object_velocity'+x+info_object.velocity;
        string_info += "</tr></tbody></table>";
    }else{
        string_info = "no information";
    }

    // generate info window
    info = new google.maps.InfoWindow();
    google.maps.event.addListener(start_info,'click',(function(start_info,string_info,info){
        return function(){
            info.setContent(string_info);
            info.open(map,start_info);
        }
    })(start_info, string_info, info));
}
function animatedCircle(index,velocity){
    var object_line =getObject(index);
    var index_line = null;
    if(object_line[2].getPath() != null){
        index_line = 2;
    }

    if(index_line != null){
        var line = object_line[index_line];
        var lineSymbol = {
            path: google.maps.SymbolPath.CIRCLE,
            scale: 8,
            strokeColor: '#393'
        };

        line.setOptions({
            icons: [{
                icon: lineSymbol,
                offset: '100%'
            }]
        });
        var count = 0;
        window.setInterval(function() {
            count = (count + 1) % 200;

            var icons = line.get('icons');
            icons[0].offset = (count / 2) + '%';
            line.set('icons', icons);
        }, parseInt(10000/velocity));
    }else{
        command_response("error: plot-map line first to generate animated");
    }
}
function addMultiMarker(index,array_data,array_attribute){
    array_marker = [];
    points =[];
    index_point = array_attribute.indexOf("point");
    // making array of points
    for(i = 0;i<array_data.length;i++){
        points.push(array_data[i][index_point]);
    }

    // making point in maps
    for(i = 0;i<points.length;i++){
        mark = addMarker(points[i]);

        // generate info string
        if(array_attribute.length>1){
            info_content="<table class=table table-bordered table-sm>";
            table_head ="<thead><tr>";
            table_body="<tbody><tr>"
            for(j=0;j<array_attribute.length;j++){
                if(j!=index_point){
                    table_head+="<th>"+array_attribute[j].toString()+"</th>";
                    table_body+="<th>"+array_data[i][j]+"</th>";
                }
            }
            table_head+="</tr></thead>";
            table_body+="</tr></tbody>";
            info_content+=table_head+table_body+"</table>";

        }else{
            info_content = "no information";
        }
        // generate info window
        info = new google.maps.InfoWindow();
        google.maps.event.addListener(mark,'click',(function(mark,info_content,info){
            return function(){
                info.setContent(info_content);
                info.open(map,mark);
            }
        })(mark, info_content, info));

        array_marker.push(mark);
    }
    
    // check and add or update to array
    if(getObject(index) != null){
        var markers = getObject(index);
        for(i = 0; i<markers.length;i++){
            markers[i].setMap(null);
        }
        object[checkIndexObject(index)].object=array_marker;
    }else{
        tuple_marker={"index":index,"object":array_marker};
        object.push(tuple_marker);
    }

    // focusing map
    focusMap(points);
}
function addMarker(position){
    marker = new google.maps.Marker({
        position: position,
        map:map
    });
    return marker;
}
function changeColorObject(index,color){
    var o = getObject(index)[2];
    o.setOptions({
        strokeColor:color
    });
    focusMap(o.getBounds());
}
function checkIndexObject(index){
    for(i = 0; i< object.length; i++){
        var path = object[i];
        if(path.index == index){
            return i;
        }
    }
    return -1;
}
function getObject(index){
    var array_index = checkIndexObject(index);
    if(array_index == -1){
        return null;
    }else{
        return object[array_index].object;
    }
}
function getCommGeomFilter(){
    var string_com = "";
    for(i=0;i<geom_getter.length;i++){
        string_com+= "ST_Intersects(point, ST_GeomFromText('POLYGON((";
        var lat_min=geom_getter[i].getBounds().getNorthEast().lat().toString();
        var lat_max=geom_getter[i].getBounds().getSouthWest().lat().toString();
        var lng_min=(geom_getter[i].getBounds().getNorthEast().lng()/2).toString();
        var lng_max=(geom_getter[i].getBounds().getSouthWest().lng()/2).toString();

        string_com+=lat_min+" "+lng_max+",";
        string_com+=lat_max+" "+lng_max+",";
        string_com+=lat_min+" "+lng_min+",";
        string_com+=lat_max+" "+lng_min+",";
        string_com+=lat_min+" "+lng_max;
        string_com+="))',4326))";
    
        if(i+1 < geom_getter.length){
            string_com+=" or "
        }
    }
    return string_com;
}

/**
 * @author rivasyafri
 */
var cells=[];
var lines=[];
var ne=null;
var sw=null;
var xNumberOfCells = null;
var yNumberOfCells = null;
var deltaX=null;
var deltaY=null;
var poly=null;
var crs = {
    "properties": {
        "name" : "EPSG:4326"
    },
    "type": "name"
};

function addRectangleGetter(){
    var bounds={
        north: map.getCenter().lat()-0.01,
        south: map.getCenter().lat()+0.01,
        east: map.getCenter().lng()+0.01,
        west: map.getCenter().lng()-0.01
    };
    poly = new google.maps.Rectangle({
        strokeColor: '#FF0000',
        strokeOpacity: 0.8,
        strokeWeight: 2,
        fillColor: '#FFFFFF',
        fillOpacity: 0.35,
        bounds: bounds,
        editable:true,
        draggable:true,
        map:map
    });
    google.maps.event.addDomListener(document, 'keyup', function (e) {
        var code = (e.keyCode ? e.keyCode : e.which);
        if (code === 8 || code === 46) {
            poly.setMap(null);
        } else if (code === 13) {
            acceptBorder();
        }
    });
    addButton();
}
function drawGridFromSelectedProject(selectedProject) {
    if (selectedProject != null) {
        if (selectedProject.area != null) {
            var bounds={
                north: selectedProject.area.bbox[0],
                west: selectedProject.area.bbox[1],
                south: selectedProject.area.bbox[2],
                east: selectedProject.area.bbox[3],
            };
            poly = new google.maps.Rectangle({
                strokeColor: '#FF0000',
                strokeOpacity: 0.8,
                strokeWeight: 2,
                fillColor: '#FFFFFF',
                fillOpacity: 0.35,
                bounds: bounds
            });
            ne = poly.getBounds().getNorthEast();
            sw = poly.getBounds().getSouthWest();
            drawGrid(selectedProject.cellSize);
        }
    }
}

/* Get from http://jsbin.com/ajimur/421/ */
function addButton() {
    poly["btnDeleteClickHandler"] = deleteBorder;
    poly["btnAcceptClickHandler"] = acceptBorder;
    poly["btnDeleteImageUrl"] = 'http://i.imgur.com/RUrKV.png';
    poly["btnAcceptImageUrl"] = 'http://image.flaticon.com/icons/svg/148/148789.svg';

    google.maps.event.addListener(poly,'bounds_changed',function () {
        var btnDelete = getButton(poly.btnDeleteImageUrl);
        var btnAccept = getButton(poly.btnAcceptImageUrl);
        if(btnDelete.length === 0) {
            var undoimg = $("img[src$='https://maps.gstatic.com/mapfiles/undo_poly.png']");
            undoimg.parent().css('height', '21px !important');
            undoimg.parent().parent().append(
                '<div style="overflow-x: hidden; overflow-y: hidden; position: absolute; width: 30px; height: 27px;top:21px;">' +
                '<img src="'+ poly.btnDeleteImageUrl +'" class="deletePoly" style="height:auto; width:auto; position: absolute; left:0;"/>' +
                '</div>'+
                '<div style="overflow-x: hidden; overflow-y: hidden; position: absolute; width: 30px; height: 27px;top:43px;left: 6px;">' +
                '<img src="'+ poly.btnAcceptImageUrl +'" class="acceptPoly" style="height:21px; width:auto; position: absolute; left:0; border: 1px solid grey;"/>' +
                '</div>'
            );

            // now get that button back again!
            btnDelete = getButton(poly.btnDeleteImageUrl);
            btnDelete.hover(function() { $(this).css('left', '-30px'); return false;},
                function() { $(this).css('left', '0px'); return false;});
            btnDelete.mousedown(function() { $(this).css('left', '-60px'); return false;});
            btnAccept = getButton(poly.btnAcceptImageUrl);
        }

        // if we've already attached a handler, remove it
        if(poly.btnDeleteClickHandler)
            btnDelete.unbind('click', poly.btnDeleteClickHandler);
        if(poly.btnAcceptClickHandler)
            btnAccept.unbind('click', poly.btnAcceptClickHandler);

        // now add a handler for removing the passed in index
        poly.btnDeleteClickHandler = deleteBorder;
        poly.btnAcceptClickHandler = acceptBorder;
        btnDelete.click(poly.btnDeleteClickHandler);
        btnAccept.click(poly.btnAcceptClickHandler);
    });
}
function acceptBorder() {
    // addJsonBorderToSelectedProject(poly);
    drawGrid(selectedProject.cellSize);
    return false;
}
function deleteBorder() {
    poly.setMap(null);
    geom_getter = [];
    return false;
}
function getButton(imageUrl) {
    return $("img[src$='" + imageUrl + "']");
}

/* Function to create grid for cellular automata */
function setBorder(){
    if (selectedProject != null) {
        if (selectedProject.cellSize != null) {
            addRectangleGetter();
        } else {
            alert('Please set the project first.');
        }
    } else {
        alert('Please load the project or create new project first.');
    }
}
function drawGrid(cellSize) {
    ne = poly.getBounds().getNorthEast();
    sw = poly.getBounds().getSouthWest();
    poly.setMap(null);
    var rectangleHeight = Math.abs(ne.lng() - sw.lng());
    var rectangleWidth = Math.abs(ne.lat() - sw.lat());
    var dividerLat = convertMToLat(cellSize);
    var avgLat = (ne.lat() + sw.lat()) / 2;
    var dividerLong = convertMToLong(cellSize, avgLat);
    xNumberOfCells = Math.round(rectangleHeight/dividerLat);
    yNumberOfCells = Math.round(rectangleWidth/dividerLong);
    deltaX = rectangleHeight/xNumberOfCells;
    deltaY = ne.lat() < 0 ? -1 * (rectangleWidth/yNumberOfCells) : rectangleWidth/yNumberOfCells ;
    // var deltaY = rectangleWidth/yNumberOfCells;
    loopCreateLines(xNumberOfCells, yNumberOfCells, deltaX, deltaY);
}
function loopCreateLines(xNumberOfCells, yNumberOfCells, deltaX, deltaY) {
    for (var y = 0; y <= yNumberOfCells; y++){
        var grid = [
            {lat: ne.lat() + deltaY * y, lng: ne.lng()},
            {lat: ne.lat() + deltaY * y, lng: sw.lng()}
        ];
        var line = new google.maps.Polyline({
            path: grid,
            strokeColor: '#000000',
            strokeOpacity: 0.8,
            strokeWeight: 2,
            map:map
        });
        lines.push(line);
    }
    for (var x = 0; x <= xNumberOfCells; x++) {
        var grid = [
            {lat: ne.lat(), lng: sw.lng() + deltaX * x},
            {lat: sw.lat(), lng: sw.lng() + deltaX * x}
        ];
        var line = new google.maps.Polyline({
            path: grid,
            strokeColor: '#000000',
            strokeOpacity: 0.8,
            strokeWeight: 2,
            map:map
        });
        lines.push(line);
    }
}
function createCellFlooded(x, y) {
    var bounds={
        north: ne.lat() + y * deltaY,
        south: ne.lat() + (y + 1) * deltaY,
        east: sw.lng() + (x + 1) * deltaX,
        west: sw.lng() + x * deltaX
    };
    var cell = new google.maps.Rectangle({
        strokeColor: '#000000',
        strokeOpacity: 0.8,
        strokeWeight: 2,
        fillColor: '#1974D2',
        fillOpacity: 0.3,
        bounds: bounds,
        map:map
    });
    cells.push(cell);
}
function clearCells() {
    cells.forEach(function (cell) {
        cell.setMap(null);
    });
    lines.forEach(function (line) {
        line.setMap(null);
    });
    cells.splice(0, cells.length);
    lines.splice(0, lines.length);
}

/* Get from http://www.coffeegnome.net/google-elevation-api-rectangle/ */
function getElevation (loc, x, y, valX, valY){
    elevator = new google.maps.ElevationService();
    var positionalRequest = {
        'locations': [loc]
    };
    elevator.getElevationForLocations(positionalRequest, function(results, status) {
        if (status == google.maps.ElevationStatus.OK) {
            if (results[0]) {
                console.log(results[0].elevation);
            } else {
                alert('No results found');
            }
        }
    });
}

/* Get from http://stackoverflow.com/questions/1253499/simple-calculations-for-working-with-lat-lon-km-distance */
function convertMToLat(m) {
    return (m/1000) / 110.574;
}
function convertMToLong(m, latInDegree) {
    return (m/1000) * Math.acos(toRadians(latInDegree))/ 111.320;
}

/* Get from http://stackoverflow.com/questions/9705123/how-can-i-get-sin-cos-and-tan-to-use-degrees-instead-of-radians */
function toRadians (angle) {
    return angle * (Math.PI / 180);
}