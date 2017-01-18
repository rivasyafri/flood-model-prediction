var serviceUrl = 'http://127.0.0.1:8080/floodsimulator-0.0.1-SNAPSHOT/';
var contentType = "application/x-www-form-urlencoded; charset=utf-8";
var selectedProject = null;
var cellSize = 2;
var timeStepInMinute = 5;
var startDate = '2016-01-01';
var intervalInMinute = 60;

// For IE user
if (window.XDomainRequest)
    contentType = "text/plain";

// Open and close the Nav
function showNav() {
    if (document.getElementById("mySidenav").style.width == '250px') {
        document.getElementById("mySidenav").style.width = "0px";
    } else {
        document.getElementById("mySidenav").style.width = "250px";
    }
}

// Refresh the selector of projects
var refreshSelect = function() {
    $.ajax({
        url: serviceUrl + "project",
        dataType: "json",
        contentType: contentType,
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            var projects = data._embedded.project;
            if (projects.length + 1 != $('#select-projects').find('option').length) {
                $('#select-projects')
                    .find('option')
                    .remove()
                    .end()
                    .append('<option selected="selected" disabled="disabled" value="none">Project Name</option>')
                ;
                projects.forEach(function (project) {
                    $('#select-projects').append($('<option>', {
                        value: project._links.self.href,
                        text: project.name
                    }));
                });
            }
        }
    });
};

// Load the project
var loadProject = function(url) {
    var request = $.ajax({
        url: url,
        dataType: "json",
        contentType: contentType,
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            selectedProject = data;
        }
    });
    return request;
};

// Create new project
var createProject = function(name) {
    var project = {
        'name' : name,
        'done' : false
    };
    var request = $.ajax({
        url: serviceUrl + 'project/',
        type: 'POST',
        contentType: "application/json",
        data: JSON.stringify(project),
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            console.log(data);
        }
    });
    return request;
};

var removeShowInModal = function () {
    $('.modal').removeClass('show');
    $('body').removeClass('modal-open');
    $('.modal-backdrop').remove();
    setTimeout(function () {
        $('.modal').css({ display: 'none'}).attr('aria-hidden', 'true');
    }, 800);
};

function drawGrid(area) {
    var ne = area.getBounds().getNorthEast();
    var sw = area.getBounds().getSouthWest();
    var rectangleHeight = Math.abs(ne.lng() - sw.lng());
    var rectangleWidth = Math.abs(ne.lat() - sw.lat());
    var divider = '0.00' + cellSize;
    var xNumberOfCells = Math.round(rectangleHeight/divider);
    var yNumberOfCells = Math.round(rectangleWidth/divider);
    var deltaX = rectangleHeight/xNumberOfCells;
    var deltaY = rectangleWidth/yNumberOfCells;
    loopCreateCell(ne, sw, xNumberOfCells, yNumberOfCells, deltaX, deltaY);
}

function loopCreateCell(ne, sw, xNumberOfCells, yNumberOfCells, deltaX, deltaY) {
    for (var y = 0; y < yNumberOfCells; y++){
        var column = [];
        for (var x = 0; x < xNumberOfCells; x++) {
            var cell = createCell(
                ne.lat() + y * deltaY,
                ne.lat() + (y+1) * deltaY,
                sw.lng() + (x+1) * deltaX,
                sw.lng() + x * deltaX
            );
            column.push(cell);
        }
        cells.push(column);
    }
}

function createCell(north, south, east, west) {
    var bounds={
        north: north,
        south: south,
        east: east,
        west: west
    };
    var geom_getter_dummy = new google.maps.Rectangle({
        strokeColor: '#000000',
        strokeOpacity: 0.8,
        strokeWeight: 2,
        fillColor: '#FFFFFF',
        fillOpacity: 0.35,
        bounds: bounds,
        map:map
    });
    return geom_getter_dummy;
}

function setCellFlooded(x, y) {
    cells[y][x].fillColor = '#1974D2';
}