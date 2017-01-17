var serviceUrl = 'http://127.0.0.1:8080/floodsimulator-0.0.1-SNAPSHOT/';
var contentType = "application/x-www-form-urlencoded; charset=utf-8";
var selectedProject = null;

// For IE user
if (window.XDomainRequest)
    contentType = "text/plain";

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
            console.log(data);
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
