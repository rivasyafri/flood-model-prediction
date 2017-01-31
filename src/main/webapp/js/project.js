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
var getOneProject = function(url) {
    var request = $.ajax({
        url: url,
        dataType: "json",
        contentType: contentType,
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            selectedProject = data;
            $('#txt_cellSize').val(selectedProject.cellSize != null ? selectedProject.cellSize : 1000);
            $('#txt_timeStep').val(selectedProject.timeStep != null ? selectedProject.timeStep : 5);
            $('#datetimepicker').val(selectedProject.startDate != null ? selectedProject.startDate : '2016-01-01T00:00:00.000Z');
            $('#txt_interval').val(selectedProject.interval != null ? selectedProject.interval : 60);
            drawGridFromSelectedProject(selectedProject);
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
var patchProject = function(project) {
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
var putProject = function() {
    var request = $.ajax({
        url: selectedProject._links.self.href,
        type: 'PUT',
        contentType: "application/json",
        data: JSON.stringify(selectedProject),
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            selectedProject = data;
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
        },
        success: function (data) {
            selectedProject = data;
        }
    });
    return request;
};
var runProject = function () {
    var request = $.ajax({
        url: serviceUrl+'project/run?id='+selectedProject.id,
        type: 'GET',
        contentType: "application/json",
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            selectedProject = data;
        }
    });
    return request;
};

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