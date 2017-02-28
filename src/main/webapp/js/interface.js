$(document).ready(function () {
    getProjects();
    $('#create-modal').load('view/create-project-modal.html');
    $('#load-modal').load('view/load-project-modal.html');
    $('#setting-modal').load('view/setting-project-modal.html');
});

/* Function for interface */
var timeElapsed = 0;
function notify(icon, message, type) {
    $.notify({
        icon: icon,
        message: message
    }, {
        type: type,
        newest_on_top: true,
        placement: {
            from: "top",
            align: "center"
        },
        timer: 1000
    });
}
function loadDataToPlaceHolder() {
    $('#txt_cellSize').val(selectedProject.cellSize != null ? selectedProject.cellSize : 100);
    $('#txt_timeStep').val(selectedProject.timeStep != null ? selectedProject.timeStep : 60);
    $('#datetimepicker-start-time').val(new Date(selectedProject.startTime * 1000));
    $('#datetimepicker-end-time').val(new Date(selectedProject.endTime * 1000));
    if (selectedProject.variable.usingDrainage) {
        $('#txt_usingDrainage').attr('checked', 'checked');
        $('#div-using-drainage').css('display', 'block').addClass('show');
        $('#div-drainage-value').css('display', 'block').addClass('show');
    } else {
        $('#div-using-drainage').css('display', 'none').removeClass('show');
        $('#div-drainage-value').css('display', 'none').removeClass('show');
    }
    if (selectedProject.variable.evapotranspirationByData) {
        $('#txt_evapotranspirationByData').attr('checked', 'checked');
        $('#div-evapotranspiration-data').css('display', 'block').addClass('show');
        $('#div-evapotranspiration-value').css('display', 'none').removeClass('show');
    } else {
        $('#div-evapotranspiration-data').css('display', 'none').removeClass('show');
        $('#div-evapotranspiration-value').css('display', 'block').addClass('show');
    }
    if (selectedProject.variable.drainageByData) {
        $('#txt_drainageByData').attr('checked', 'checked');
        $('#div-drainage-data').css('display', 'block').addClass('show');
        $('#div-drainage-value').css('display', 'none').removeClass('show');
    } else {
        $('#div-drainage-data').css('display', 'none').removeClass('show');
        $('#div-drainage-value').css('display', 'block').addClass('show');
    }
    if (selectedProject.variable.usingEvapotranspiration) {
        $('#txt_usingEvapotranspiration').attr('checked', 'checked');
        $('#div-using-evapotranspiration').css('display', 'block').addClass('show');
        $('#div-evapotranspiration-value').css('display', 'block').addClass('show');
    } else {
        $('#div-using-evapotranspiration').css('display', 'none').removeClass('show');
        $('#div-evapotranspiration-value').css('display', 'none').removeClass('show');
    }
    $('#txt_usingDrainage').val(selectedProject.variable.usingDrainage);
    $('#txt_drainageByData').val(selectedProject.variable.drainageByData);
    $('#txt_usingEvapotranspiration').val(selectedProject.variable.usingEvapotranspiration);
    $('#txt_evapotranspirationByData').val(selectedProject.variable.evapotranspirationByData);
    $('#txt_drainageValue').val(selectedProject.variable.drainageValue);
    $('#txt_discharge').val(selectedProject.variable.discharge);
    $('#txt_side').val(selectedProject.variable.side);
    $('#txt_evapotranspirationValue').val(selectedProject.variable.evapotranspirationValue);
    $('#txt_radiation').val(selectedProject.variable.radiation);
    $('#txt_geothermal').val(selectedProject.variable.geothermal);
    $('#txt_delta').val(selectedProject.variable.delta);
    $('#txt_cn').val(selectedProject.variable.cn);
    $('#txt_cd').val(selectedProject.variable.cd);
    $('#txt_saturatedWaterVapor').val(selectedProject.variable.saturatedWaterVapor);
    $('#txt_waterVapor').val(selectedProject.variable.waterVapor);
    $('#txt_windSpeed').val(selectedProject.variable.windSpeed);
    $('#txt_meanTemperature').val(selectedProject.variable.meanTemperature);
    $('#txt_psychometric').val(selectedProject.variable.psychometric);
    $('#select-model').val(selectedProject.model);
    if (selectedProject.model == 'Chen') {
        $('#select-model option[value="Chen"]').prop("selected", true);
    } else if (selectedProject.model == 'VIC') {
        $('#select-model option[value="VIC"]').prop("selected", true);
    } else {
        $('#select-model option[value="Prasetya"]').prop("selected", true);
    }
}
function sideNavValidation() {
    if (selectedProject != null) {
        $('#mySidenav').append('<a href="javascript:void(0)" onclick="showNav(); buttonSelectBorder();">Set Border</a>' +
            '<a href="javascript:void(0)" data-toggle="modal" data-target="#variable-modal" onclick="showNav()">Set Variables</a>' +
            '<a href="javascript:void(0)" data-toggle="modal" data-target="#setting-modal" onclick="showNav()">Setting</a>'
        );
    } else {
        $('#mySidenav')
            .find('a')
            .remove()
            .end()
            .append('<a href="javascript:void(0)" class="closebtn" onclick="showNav()">&times;</a>' +
                '<a href="javascript:void(0)" data-toggle="modal" data-target="#create-modal" onclick="showNav()">Create Project</a>' +
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
        $('#button_set_area').prop('disabled', false);
        $('#button_remove_area').prop('disabled', false);
        $('#button_setting').prop('disabled', false);
    } else {
        $('#button_play').prop('disabled', true);
        $('#button_close').prop('disabled', true);
        $('#button_save').prop('disabled', true);
        $('#button_stop').prop('disabled', true);
        $('#button_set_area').prop('disabled', true);
        $('#button_remove_area').prop('disabled', true);
        $('#button_setting').prop('disabled', true);
    }
}
function buttonSelectBorder() {
    if (selectedProject != null) {
        if (selectedProject.area != null) {
            drawGridFromSelectedProject();
        } else if (selectedProject.cellSize != null) {
            addRectangleGetter();
        } else {
            alert('Please set the project first.');
        }
    } else {
        alert('Please load the project or create new project first.');
    }
}
function buttonSavePress() {
    var request = setBorderAPI(poly.getBounds().getNorthEast(),
        poly.getBounds().getSouthWest());
    request.done(function (response, textStatus, jqXHR) {
        notify("fa fa-check-circle-o", selectedProject.name + " borders are successfully saved to database.", textStatus);
        var load = getOne(selectedProject._links.self.href);
        load.done(function (response, textStatus, jqXHR) {
            selectedProject = response;
            console.log(selectedProject);
            notify("fa fa-check-circle-o", selectedProject.name + " is loaded successfully.", textStatus);
            loadDataToPlaceHolder();
            showPlayer();
            sideNavValidation();
        });
        load.fail(function (response, textStatus, jqXHR) {
            notify("fa fa-times-circle-o", selectedProject.name + " cannot be loaded. See log.", "danger");
        });
    });
    request.fail(function (response, textStatus, jqXHR) {
        notify("fa fa-times-circle-o", selectedProject.name + " borders cannot be saved. See log.", "danger");
    });
}
function buttonRemoveBorder() {
    selectedProject.area = null;
    clearCells();
    console.log(selectedProject);
    var request = putProject(selectedProject);
    request.done(function (response, textStatus, jqXHR) {
        alert(textStatus);
        console.log(response);
    });
}
function buttonStopPress() {
    // if (cells != null) {
    //     cells.forEach(function (cell) {
    //         cell.setOptions({fillColor: '#FFFFFF', fillOpacity: 0.1});
    //     });
    // }
}
function buttonPlayPress() {
    if (typeof matrix != 'undefined' && matrix != null) {
        var requestCellStates = getCellStatesSortedByStartTime();
        requestCellStates.done(function (response, textStatus, jqXHR) {
            selectedProject.cellStates = response._embedded.cell_state;
            console.log(selectedProject);
            var play = false;
            if (selectedProject.cellStates != null) {
                console.log("tes");
                if (selectedProject.cellStates.length != 0) {
                    console.log("lanjut");
                    console.log("play result");
                    var requestInvertedCellStates = getCellStatesSortedByEndTime();
                    requestInvertedCellStates.done(function (response, textStatus, jqXHR) {
                        var invertedCellStates = response._embedded.cell_state;
                        notify("fa fa-check-circle-o", selectedProject.name + " is currently running.", textStatus);
                        timeElapsed = selectedProject.startTime;
                        var j = 0;
                        var i = 0;
                        var k = 0;
                        while ((j < invertedCellStates.length || i < selectedProject.cellStates.length) &&
                        timeElapsed < selectedProject.endTime) {
                            console.log(selectedProject.startTime + " " + selectedProject.endTime);
                            console.log(i + " " + j + " in " + timeElapsed);
                            var cellState = selectedProject.cellStates[i];
                            if (typeof cellState != 'undefined' && cellState != null) {
                                while (timeElapsed == cellState.startTime && i < selectedProject.cellStates.length) {
                                    (function (cellState, k) {
                                        setTimeout(function () {
                                            console.log("normal : " + cellState.xarray + " " + cellState.yarray + " " + cellState.startTime + " " + cellState.endTime);
                                            createFloodedCell(cellState.xarray, cellState.yarray);
                                        }, k * 4000);
                                    })(cellState, k);
                                    i++;
                                    cellState = selectedProject.cellStates[i];
                                    if (typeof cellState == 'undefined' || cellState == null) {
                                        break;
                                    }
                                }
                            }
                            var invertedCellState = invertedCellStates[j];
                            if (typeof invertedCellState != 'undefined' && invertedCellState != null) {
                                while (timeElapsed == invertedCellState.endTime && j < invertedCellStates.length) {
                                    (function (invertedCellState, k) {
                                        setTimeout(function () {
                                            console.log("inverted : " + invertedCellState.xarray + " " + invertedCellState.yarray + " " +
                                                invertedCellState.startTime + " " + invertedCellState.endTime);
                                            removeFloodedCell(invertedCellState.xarray, invertedCellState.yarray);
                                        }, k * 4000 + 2000);
                                    })(invertedCellState, k);
                                    j++;
                                    invertedCellState = invertedCellStates[j];
                                    if (typeof invertedCellState == 'undefined' || invertedCellState == null) {
                                        break;
                                    }
                                }
                            }
                            timeElapsed += selectedProject.timeStep;
                            k++;
                        }
                        if (timeElapsed < selectedProject.endTime) {
                            setTimeout(function () {
                                buttonPlayPress();
                            }, k * 4000 + 2000);
                        }
                    });
                } else {
                    play = true;
                }
            } else {
                play = true;
            }
            if (play) {
                console.log("prediction start");
                var request = runProject();
                notify("fa fa-check-circle-o", "Prediction for '" + selectedProject.name + "' is running.", textStatus);
                request.done(function (response, textStatus, jqXHR) {
                    // setTimeout(function () {
                    //     buttonPlayPress();
                    // }, 3000);
                });
            }
        });
    }
}
function buttonClosePress() {
    selectedProject = null;
    clearCells();
    alert('Selected Project is Cleared.');
    showPlayer();
    sideNavValidation();
}
