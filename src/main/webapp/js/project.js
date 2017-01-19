var serviceUrl = 'http://127.0.0.1:8080/floodsimulator-0.0.1-SNAPSHOT/';
var contentType = "application/x-www-form-urlencoded; charset=utf-8";
var selectedProject = null;
var cellSize = 1000;
var timeStepInMinute = 5;
var startDate = '2016-01-01';
var intervalInMinute = 60;
var cells=[];

// For IE user
if (window.XDomainRequest)
    contentType = "text/plain";

var loadProjects = function() {
    $.ajax({
        url: serviceUrl + "project",
        dataType: "json",
        contentType: contentType,
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            refreshSelect(data._embedded.project);
        }
    });
};

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
            cellSize = selectedProject.cellSize != null ? selectedProject.cellSize : 1000;
            timeStepInMinute = selectedProject.timeStep != null ? selectedProject.timeStep : 5;
            startDate = selectedProject.startDate != null ? selectedProject.startDate : '2016-01-01T00:00:00.000Z';
            intervalInMinute = selectedProject.interval != null ? selectedProject.interval : 60;
        }
    });
    return request;
};

var createProject = function(project) {
    var request = $.ajax({
        url: serviceUrl + 'project/',
        type: 'POST',
        contentType: "application/json",
        data: project,
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            selectedProject = data;
        }
    });
    return request;
};

var deleteProject = function(url) {
    var request = $.ajax({
        url: url,
        type: 'DELETE',
        xhrFields: {
            withCredentials: false
        }
    });
    return request;
};

var editProject = function(project) {
    var request = $.ajax({
        url: selectedProject._links.self.href,
        type: 'PATCH',
        contentType: "application/json",
        data: project,
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            selectedProject = data;
        }
    });
    return request;
};

/* Function to create grid for cellular automata */
function setBorder(){
    if (selectedProject != null) {
        addRectangleGetter();
    }
}

function drawGrid(area) {
    var ne = area.getBounds().getNorthEast();
    var sw = area.getBounds().getSouthWest();
    var rectangleHeight = Math.abs(ne.lng() - sw.lng());
    var rectangleWidth = Math.abs(ne.lat() - sw.lat());
    var dividerLat = convertMToLat(cellSize);
    var avgLat = (ne.lat() + sw.lat()) / 2;
    var dividerLong = convertMToLong(cellSize, avgLat);
    var xNumberOfCells = Math.round(rectangleHeight/dividerLat);
    var yNumberOfCells = Math.round(rectangleWidth/dividerLong);
    var deltaX = rectangleHeight/xNumberOfCells;
    var deltaY = ne.lat() < 0 ? -1 * (rectangleWidth/yNumberOfCells) : rectangleWidth/yNumberOfCells ;
    area.setMap(null);
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
        fillOpacity: 0.1,
        bounds: bounds,
        map:map
    });
    return geom_getter_dummy;
}

function buttonPlayPress(){
    for (var i = 0; i < 10; i++) {
        var x = Math.floor(Math.random() * cells[0].length);
        var y = Math.floor(Math.random() * cells.length);
        setCellFlooded(x, y);
    }
}

function setCellFlooded(x, y) {
    console.log(cells[y][x]);
    // cells[y][x].fillColor = '#1974D2';
    // cells[y][x].fillOpacity = 0.9;
    cells[y][x].setOptions({fillColor: '#1974D2', fillOpacity:0.3})
}

function removeCells() {
    cells.forEach(function (column) {
        column.forEach(function (cell) {
            cell.setMap(null);
        })
    })
}

/* Function for interface */
function removeShowInModal () {
    $('.modal').removeClass('show');
    $('body').removeClass('modal-open');
    $('.modal-backdrop').remove();
    setTimeout(function () {
        $('.modal').css({ display: 'none'}).attr('aria-hidden', 'true');
    }, 800);
}

function refreshSelect(projects) {
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

function showNav() {
    if (document.getElementById("mySidenav").style.width == '250px') {
        document.getElementById("mySidenav").style.width = "0px";
    } else {
        document.getElementById("mySidenav").style.width = "250px";
    }
}

/* Get from http://stackoverflow.com/questions/1184624/convert-form-data-to-javascript-object-with-jquery */
$.fn.serializeObject = function()
{
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};