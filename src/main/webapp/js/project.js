/**
 * @author rivasyafri
 */
var serviceUrl = 'http://127.0.0.1:8080/';
var contentType = "application/x-www-form-urlencoded; charset=utf-8";

/* Projects variable */
var selectedProject = null;

/* For IE user */
if (window.XDomainRequest) {
    contentType = "text/plain";
}

/* Method for Projects */
var getProjects = function() {
    return $.ajax({
        url: serviceUrl + "project",
        dataType: "json",
        contentType: contentType,
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            refreshSelect(data._embedded.project);
            console.log(data);
        }
    });
};
var getCellStatesSortedByStartTime = function() {
    return $.ajax({
        url: serviceUrl + "cell_state/search/findByProjectIdWithSorting?id="
            + selectedProject.id +"&sort=startTime,asc",
        dataType: "json",
        contentType: contentType,
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            console.log(data);
        }
    });
};
var getCellStatesSortedByEndTime = function() {
    return $.ajax({
        url: serviceUrl + "cell_state/search/findByProjectIdWithSorting?id="
        + selectedProject.id +"&sort=endTime,asc",
        dataType: "json",
        contentType: contentType,
        xhrFields: {
            withCredentials: false
        }
    });
};
var getOne = function(url) {
    var request = $.ajax({
        url: url,
        dataType: "json",
        contentType: contentType,
        xhrFields: {
            withCredentials: false
        }
    });
    return request;
};
var postProject = function(project) {
    var request = $.ajax({
        url: serviceUrl + 'project/',
        type: 'POST',
        contentType: "application/json",
        data: project,
        xhrFields: {
            withCredentials: false
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
var patchProject = function(project) {
    var request = $.ajax({
        url: selectedProject._links.self.href,
        type: 'PATCH',
        contentType: "application/json",
        data: project,
        xhrFields: {
            withCredentials: false
        }
    });
    return request;
};
var putProject = function() {
    var request = $.ajax({
        url: selectedProject._links.self.href,
        type: 'PUT',
        contentType: "application/json",
        data: JSON.stringify(selectedProject),
        xhrFields: {
            withCredentials: false
        }
    });
    return request;
};
var setBorderAPI = function (ne, sw) {
    var request = $.ajax({
        url: serviceUrl+'project/setBorder?id='+selectedProject.id+
            "&north="+ne.lat()+
            "&west="+sw.lng()+
            "&south="+sw.lat()+
            "&east="+ne.lng(),
        type: 'POST',
        contentType: "application/json",
        xhrFields: {
            withCredentials: false
        }
    });
    return request;
};
var runProject = function () {
    var request = $.ajax({
        url: serviceUrl+'project/run?id='+selectedProject.id,
        type: 'POST',
        contentType: "application/json",
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            selectedProject = typeof data !== 'undefined' ? data : selectedProject;
        }
    });
    return request;
};
var resetProject = function () {
    var request = $.ajax({
        url: serviceUrl+'project/reset?id='+selectedProject.id,
        type: 'POST',
        contentType: "application/json",
        xhrFields: {
            withCredentials: false
        }
    });
    return request;
};
function drawGridFromSelectedProject() {
    console.log("draw grid from selected project")
    var bounds={
                north: selectedProject.area.bbox[3],
                west: selectedProject.area.bbox[2],
                south: selectedProject.area.bbox[1],
                east: selectedProject.area.bbox[0],
            };
    poly = new google.maps.Rectangle({
        strokeColor: '#FF0000',
        strokeOpacity: 0.8,
        strokeWeight: 2,
        fillColor: '#FFFFFF',
        fillOpacity: 0.35,
        bounds: bounds,
        map:map
    });
    ne = poly.getBounds().getNorthEast();
    sw = poly.getBounds().getSouthWest();
    drawGrid(selectedProject.cellSize);
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