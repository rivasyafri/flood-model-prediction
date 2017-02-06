$(document).ready(function () {
    getProjects();
    $('#create-modal').load('view/create-project-modal.html');
    $('#load-modal').load('view/load-project-modal.html');
    $('#setting-modal').load('view/setting-project-modal.html');
});

/* Function for interface */
function sideNavValidation() {
    if (selectedProject != null) {
        $('#mySidenav').append('<a href="javascript:void(0)" onclick="showNav(); setBorder();">Set Border</a>' +
            '<a href="javascript:void(0)" data-toggle="modal" data-target="#variable-modal" onclick="showNav()">Set Variables</a>' +
            '<a href="javascript:void(0)" data-toggle="modal" data-target="#setting-modal" onclick="showNav()">Setting</a>'
        );
    } else {
        $('#mySidenav')
            .find('a')
            .remove()
            .end()
            .append('<a href="javascript:void(0)" class="closebtn" onclick="showNav()">&times;</a>'+
                '<a href="javascript:void(0)" data-toggle="modal" data-target="#create-modal" onclick="showNav()">Create Project</a>'+
                '<a href="javascript:void(0)" data-toggle="modal" data-target="#load-modal" onclick="showNav()">Load Project</a>'
            );
    }
}
function refreshSelect(projects) {
    if (projects.length + 1 != $('#select-projects').find('option').length) {
        $('#select-projects')
            .find('option')
            .remove()
            .end()
            .append('<option selected="selected" disabled="disabled" value="none">Project Name</option>');
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
function showPlayer() {
    if (selectedProject != null) {
        $('#button_play').prop('disabled', false);
        $('#button_close').prop('disabled', false);
        $('#button_save').prop('disabled', false);
        $('#button_stop').prop('disabled', false);
        $('#button_box').prop('disabled', false);
        $('#button_test').prop('disabled', false);
        $('#button_setting').prop('disabled', false);
    } else {
        $('#button_play').prop('disabled', true);
        $('#button_close').prop('disabled', true);
        $('#button_save').prop('disabled', true);
        $('#button_stop').prop('disabled', true);
        $('#button_box').prop('disabled', true);
        $('#button_test').prop('disabled', true);
        $('#button_setting').prop('disabled', true);
    }
}
function buttonPlayPress() {
    // if (lines != null){
    //     for (var i = 0; i < 10; i++) {
    //         var x = Math.floor(Math.random() * xNumberOfCells);
    //         var y = Math.floor(Math.random() * yNumberOfCells);
    //         createCellFlooded(x, y);
    //     }
    // }
    var request = runProject();
    request.done(function (response, textStatus, jqXHR) {
        alert(textStatus);
        console.log(response);
    });
}
function buttonStopPress() {
    if (cells != null) {
        cells.forEach(function (cell) {
            cell.setOptions({fillColor: '#FFFFFF', fillOpacity:0.1});
        });
    }
}
function buttonSavePress() {
    var request = setBorderAPI(poly.getBounds().getNorthEast(),
        poly.getBounds().getSouthWest());
    request.done(function (response, textStatus, jqXHR) {
        alert(textStatus);
        console.log(response);
    });
}
function buttonTestPress() {
    var center = {};
    var x1 = sw.lng();
    var y1 = sw.lat();
    var x2 = ne.lng();
    var y2 = ne.lat();
    center.x = x1 + ((x2 - x1) / 2);
    center.y = y1 + ((y2 - y1) / 2);
    var mPosition = new google.maps.LatLng(center.y, center.x);
    getElevation(mPosition);
}
function buttonClosePress() {
    selectedProject = null;
    clearCells();
    alert('Selected Project is Cleared.');
    showPlayer();
    sideNavValidation();
}
