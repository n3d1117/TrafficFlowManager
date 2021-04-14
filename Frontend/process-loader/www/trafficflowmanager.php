<!DOCTYPE html>

<?php

include('config.php'); // Includes Login Script
include('external_service.php');

if (isset($_SESSION['accessToken'])) {

    // METADATA API
    $url_api = $host_trafficflowmanager . 'trafficflowmanager/api/metadata';
    $json_api = file_get_contents($url_api);
    $list_api = json_decode($json_api);

    // QUERY COLOR MAPS
    $link = mysqli_connect($host_heatmap, $username_heatmap, $password_heatmap) or die("failed to connect to server !!");
    mysqli_set_charset($link, 'utf8');
    mysqli_select_db($link, $dbname_heatmap);
    $process_cm = array();
    $query_cm = "SELECT DISTINCT metric_name FROM heatmap.colors";
    $result_cm = mysqli_query($link, $query_cm) or die(mysqli_error($link));
    if ($result_cm->num_rows >0){
        while ($row_cm = mysqli_fetch_assoc($result_cm)) {
            $listFile_cm = array(
                "metric_name" => $row_cm['metric_name']
            );
            array_push($process_cm, $listFile_cm);
        }
        $total_cm=$result_cm->num_rows;
    }
}

if (isset ($_SESSION['username'])){
    $role_att = $_SESSION['role'];
} else {
    $role_att = "";
}

?>

<html lang="en">

<!-- HEAD -->
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>TrafficFlow Manager</title>

    <!-- jQuery -->
    <script src="jquery/jquery-1.10.1.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="bootstrap/bootstrap.min.js"></script>

    <!-- Bootstrap Core CSS -->
    <link href="bootstrap/bootstrap.css" rel="stylesheet">

    <!-- Bootstrap toggle button -->
    <link href="bootstrapToggleButton/css/bootstrap-toggle.min.css" rel="stylesheet">
    <script src="bootstrapToggleButton/js/bootstrap-toggle.min.js"></script>

    <!-- Dynatable -->
    <link rel="stylesheet" href="dynatable/jquery.dynatable.css">
    <script src="dynatable/jquery.dynatable.js"></script>

    <!-- Font awesome icons -->
    <link rel="stylesheet" href="fontAwesome/css/font-awesome.min.css">

    <!-- Custom CSS -->
    <link href="css/dashboard.css" rel="stylesheet">
    <link href="css/dashboardList.css" rel="stylesheet">

    <!-- Color Picker -->
    <script src="bootstrap-colorpicker/dist/js/bootstrap-colorpicker.js"></script>
    <script src="bootstrap-colorpicker/dist/js/bootstrap-colorpicker.min.js"></script>
    <!-- -->
    <link href="bootstrap-colorpicker/dist/css/bootstrap-colorpicker.css" rel="stylesheet" />
    <link href="bootstrap-colorpicker/dist/css/bootstrap-colorpicker.min.css" rel="stylesheet" />
    <!-- -->

    <!-- Tabella -->
    <script type="text/javascript" charset="utf8" src="lib/datatables.js"></script>
    <script type="text/javascript" src="lib/dataTables.responsive.min.js"></script>
    <script type="text/javascript" src="lib/dataTables.bootstrap4.min.js"></script>
    <script type="text/javascript" src="lib/jquery.dataTables.min.js"></script>
    <link href="lib/responsive.dataTables.css" rel="stylesheet" />

</head>

<!-- STYLE -->
<style>

    .hidden {
        display: none;
    }

    #view-modal {
        width: auto;
    }

    #value_table {
        width: 100%;
    }

    td {
        vertical-align: middle;
    }

    #trafficflow_table {
        margin-bottom: 0px;
    }

    .loader {
        border: 16px solid #f3f3f3; /* Light grey */
        border-top: 16px solid #3498db; /* Blue */
        border-radius: 50%;
        width: 120px;
        height: 120px;
        animation: spin 2s linear infinite;
        position: absolute;
        left: 50%;
        right: 50%;
    }

    @keyframes spin {
        0% { transform: rotate(0deg); }
        100% { transform: rotate(360deg); }
    }

    .active {
        background-color: rgba(138, 159, 168, 1);
    }

</style>

<!-- BODY -->
<body class="guiPageBody">

    <!-- Main content -->
    <div class="container-fluid">
        <div class="row mainRow" style='background-color: rgba(138, 159, 168, 1)'>
            <?php include "mainMenu.php" ?>
            <div class="col-xs-12 col-md-10" id="mainCnt">
                <div class="row">
                    <div class="col-xs-12" id="mainContentCnt" style='background-color: rgba(138, 159, 168, 1); padding-top:20px;'>
                        <table id="trafficflow_table" class="table table-striped table-bordered display responsive no-wrap" style="width: 100%;">

                            <!-- Header -->
                            <thead class="dashboardsTableHeader">
                                <tr>
                                    <th><div><a>Flux Name</a></div></th>
                                    <th><div><a>Locality</a></div></th>
                                    <th><div><a>Organization</a></div></th>
                                    <th><div><a>Scenario</a></div></th>
                                    <th><div><a>Instances</a></div></th>
                                    <th><div><a>View Data</a></div></th>
                                    <th><div><a>Metric</a></div></th>
                                    <th><div><a>ColorMap</a></div></th>
                                    <th><div><a>Delete</a></div></th>
                                    <th><div><a>Unit of Measure</a></div></th>
                                    <th><div><a>Static Graph Name</a></div></th>
                                </tr>
                            </thead>

                            <!-- Rows -->
                            <tbody>
                            <?php
                            for ($i = 0; $i < count($list_api); $i++) {
                                echo("<tr>");
                                echo("<td>" . $list_api[$i]->fluxName . "</td>");
                                echo("<td>" . $list_api[$i]->locality . "</td>");
                                echo("<td>" . $list_api[$i]->organization . "</td>");
                                echo("<td>" . $list_api[$i]->scenarioID . "</td>");
                                echo("<td>" . $list_api[$i]->instances . "</td>");
                                echo("<td><button type='button' class='viewDashBtn viewList' data-target='#view-modal' data-toggle='modal' value='".$list_api[$i]->fluxName."'>VIEW</button></td>");
                                echo("<td>" . $list_api[$i]->metricName . "</td>");
                                echo("<td>
                                        <button type='button' class='viewDashBtn viewType' data-target='#typology-modal' data-toggle='modal' value='". $list_api[$i]->colorMap ."'>VIEW</button>  
									    <button type='button' class='editDashBtn editColor' data-target='#edit-colormap' data-toggle='modal' map_name='". $list_api[$i]->fluxName ."' value='". $list_api[$i]->colorMap ."'>EDIT</button>
										<p style='display: inline; margin-left: 2%;'>". $list_api[$i]->colorMap ."</p>
									  </td>");
                                echo("<td><button type='button' class='delDashBtn del_metdata' data-target='#delete-modal' data-toggle='modal' value='". $list_api[$i]->fluxName ."'>DEL</button></td>");
                                echo("<td>" . $list_api[$i]->unitOfMeasure . "</td>");
                                echo("<td>" . $list_api[$i]->staticGraphName . "</td>");
                                echo("</tr>");
                            }
                            ?>
                            </tbody>
                        </table>
                    </div>

                    <!-- View Data Modal -->
                    <div class="modal fade bd-example-modal-lg" id="view-modal" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true">
                        <div class="modal-dialog modal-lg">
                            <form name="View Metadata" method="post" action="#" id="list_Heatmap">
                                <div class="modal-content">
                                    <div class="modal-header" style="background-color: white" id="list_header">Traffic Flow Instances List: </div>
                                    <div class="modal-body" style="background-color: white">
                                        <div>
                                            <table id="value_table" class="table table-striped table-bordered" style="width: 100%;">
                                                <thead class="dashboardsTableHeader">
                                                    <th><div><a>Date</a></div></th>
                                                    <th><div><a>Duration</a></div></th>
                                                    <th><div><a>Layer Name</a></div></th>
                                                    <th><div><a>Delete</a></div></th>
                                                </thead>
                                                <tbody>
                                                </tbody>
                                            </table>
                                        </div>
                                        <div>
                                            <p id="data_content"></p>
                                        </div>
                                    </div>
                                    <div class="loader"></div>
                                    <p id="corrent" style="display:none;"></p>
                                    <div id="link_list" style="text-align: center;">
                                    </div>
                                    <div class="modal-footer" style="background-color: white">
                                        <button type="button" class="btn cancelBtn" id="list_close" data-dismiss="modal">Cancel</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>

                    <!-- View Color Map Modal -->
                    <div class="modal fade bd-example-modal-lg" id="typology-modal" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true">
                        <div class="modal-dialog">
                            <form name="View Color Map" method="post" action="#" id="typology_Heatmap">
                                <div class="modal-content">
                                    <div class="modal-header" style="background-color: white" id="typology_header">Color map </div>
                                    <div class="modal-body" style="background-color: white">
                                        <div>
                                            <table id="typology_table" class="table table-striped table-bordered" style="width: 100%;">
                                                <thead class="dashboardsTableHeader">
                                                <th class="min">
                                                    <div><a>Minimum</a></div>
                                                </th>
                                                <th class="max">
                                                    <div><a>Maximum</a></div>
                                                </th>
                                                <th class="rgb">
                                                    <div><a>Rgb</a></div>
                                                </th>
                                                <th class="color">
                                                    <div><a>Color</a></div>
                                                </th>
                                                <th class="order">
                                                    <div><a>Order</a></div>
                                                </th>
                                                </thead>
                                                <tbody>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="modal-footer" style="background-color: white">
                                        <button type="button" class="btn cancelBtn" id="typology_close" data-dismiss="modal">Cancel</button>
                                        <!--<input type="submit" value="Confirm" class="btn confirmBtn"/>-->
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>

                    <!-- Edit Color Map Modal -->
                    <div class="modal fade bd-example-modal-lg" id="edit-colormap" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true">
                        <div class="modal-dialog modal-sm">
                            <form name="Edit Color Map" method="post" action="#" id="color_Heatmap">
                                <div class="modal-content">
                                    <div class="modal-header" style="background-color: white" id="color_header">Select Color map: </div>
                                    <div class="modal-body" style="background-color: white">
                                        <input type="text" id="id_colormap" class="hidden">
                                        <div class="form-group">
                                            <label for="exampleFormControlSelect1">Select Color Map:</label>
                                            <select class="form-control" id="colorMapList">
                                                <?php
                                                for ($z=0; $z<$total_cm; $z++) {
                                                    echo('<option>'.$process_cm[$z]['metric_name'].'</option>');
                                                }
                                                ?>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="modal-footer" style="background-color: white">
                                        <button type="button" class="btn cancelBtn" id="color_close" data-dismiss="modal">Cancel</button>
                                        <input type="button" value="Confirm" class="btn confirmBtn" id="edit_color_map"/>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>

                    <!-- Delete Heatmap Modal -->
                    <div class="modal fade bd-example-modal-lg" id="delete-modal" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true">
                        <div class="modal-dialog">
                            <form name="Delete Heatmap" method="post" action="#" id="delete_Heatmap">
                                <div class="modal-content">
                                    <div class="modal-header" style="background-color: white">Delete Scenario</div>
                                    <div class="modal-body" style="background-color: white">
                                        <p id="id_delete_modal_text">Are you sure do you want delete this scenario?</p>
                                        <div>
                                            <input type="text" id="id_heat" class="hidden">
                                        </div>
                                    </div>
                                    <div class="modal-footer" style="background-color: white">
                                        <button type="button" class="btn cancelBtn" data-dismiss="modal">Cancel</button>
                                        <input type="button" value="Confirm" class="btn confirmBtn" id="delete_heatmap" />
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>

                    <!-- Delete Data Modal -->
                    <div class="modal bd-example-modal-lg" id="data_elimination" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true">
                        <div class="modal-dialog">
                            <form name="Delete Data" method="post" action="#">
                                <div class="modal-content">
                                    <div class="modal-header" style="background-color: white" id="colormap_header">Delete Data</div>
                                    <div class="modal-body" style="background-color: white">
                                        <p id="id_confirm_delete_data_text">Are you sure do you want delete this Data from the Heatmap?</p>
                                        <div>
                                            <input type="text" id="id_layerName" class="hidden">
                                            <input type="text" id="id_fluxName" class="hidden">
                                        </div>
                                    </div>
                                    <div class="modal-footer" style="background-color: white">
                                        <button type="button" class="btn cancelBtn" id="deleteData_close" data-dismiss="modal">Cancel</button>
                                        <input type="button" value="Confirm" class="btn confirmBtn" id="confirmDeletedData" />
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>

    <!-- JavaScript -->
    <script type='text/javascript'>

        const host_trafficflowmanager = "<?=$host_trafficflowmanager;?>";
        const role = "<?=$role_att;?>";

        $(document).ready(function() {

            // Authentication
            if (role == "") {
                $(document).empty();
                //window.alert("You need to log in to access to this page!");
                if (window.self !== window.top) {
                    window.location.href = 'https://www.snap4city.org/auth/realms/master/protocol/openid-connect/auth?response_type=code&redirect_uri=https%3A%2F%2Fmain.snap4city.org%2Fmanagement%2FssoLogin.php%3Fredirect%3DiframeApp.php%253FlinkUrl%253Dhttps%253A%252F%252Fwww.snap4city.org%252Fdrupal%252Fopenid-connect%252Flogin%2526linkId%253Dsnap4cityPortalLink%2526pageTitle%253Dwww.snap4city.org%2526fromSubmenu%253Dfalse&client_id=php-dashboard-builder&nonce=be3aea0a2bb852217929cbb639370c9e&state=d090eb830f9abb504fd7012f6a12389c&scope=openid+username+profile';
                } else {
                    window.location.href = 'page.php?pageTitle=Process%20Loader:%20View%20Resources';
                }
            }

            $('#trafficflow_table').DataTable({
                "searching": true,
                "paging": true,
                "ordering": true,
                "info": false,
                "responsive": true,
                "lengthMenu": [5, 10, 15],
                "iDisplayLength": 10,
                "pagingType": "full_numbers",
                "dom": '<"pull-left"l><"pull-right"f>tip',
                "language": {
                    "paginate": {
                        "first": "First",
                        "last": "Last",
                        "next": "Next >>",
                        "previous": "<< Prev"
                    },
                    "lengthMenu": "Show	_MENU_ ",
                }
            });

            // Function called when clicking 'View' in VIEW DATA column
            $(document).on('click', '.viewList', function() {
                const flux_name = $(this).val();
                load_heatmap_data(flux_name);
            });

            // Function called when clicking 'View' in COLOR MAP column
            $(document).on('click', '.viewType', function() {
                const metric = $(this).val();
                $('#typology_header').text('Color Map:	' + metric);
                $.ajax({
                    url: 'get_heatmap.php',
                    data: {
                        metric: metric,
                        action: 'color_map'
                    },
                    type: "POST",
                    async: true,
                    dataType: 'json',
                    success: function(data) {

                        for (var i = 0; i < data.length; i++) {
                            var data_id = data[i]['id'];
                            var rgb = data[i]['rgb'];
                            var color = data[i]['color'];
                            var order = data[i]['order'];
                            var min = data[i]['min'];
                            var max = data[i]['max'];
                            if (min == null) {
                                min = '';
                            }
                            if (max == null) {
                                max = '';
                            }

                            rgb0 = rgb.replace("[", "(");
                            rgb = rgb0.replace("]", ")");

                            $('#typology_table tbody').append('<tr><td>' + min + '</td><td>' + max + '</td><td>' + rgb + ' <p><i class="fa fa-circle" style="color: rgb' + rgb + '"></i></p></td><td>' + color + '</td><td>' + order + '</td></tr>');
                        }
                        //
                        $('#typology_table').DataTable({
                            "searching": false,
                            "paging": false,
                            "ordering": false,
                            "info": false,
                            "responsive": true,
                            "bDestroy": true
                        });
                    }
                });
            });

            // Function called when dismissing VIEW COLOR MAP modal
            $(document).on('click', '#typology_close', function() {
                $('#typology_table tbody').empty();
            });

            // Function called when clicking 'Edit' in COLOR MAP column
            $(document).on('click', '.editColor', function() {
                const metric = $(this).val();
                const map_name = $(this).attr('map_name');
                $('#color_header').text('Edit Color Map:	' + map_name);
                $('#id_colormap').val(map_name);
                $('#colorMapList').val(metric);
            });

            // Function called when confirming EDIT COLOR MAP action
            $(document).on('click', '#edit_color_map', function() {
                const valore = $('#colorMapList').val();
                const id = $('#id_colormap').val();
                $.ajax({
                    url: host_trafficflowmanager + 'trafficflowmanager/api/metadata',
                    data: {
                        id: id,
                        valore: valore,
                        action: 'change_color_map'
                    },
                    type: "POST",
                    async: true,
                    success: function() {
                        $('#color_Heatmap').modal('hide');
                        location.reload();
                    }
                });
            });

            // Function called when dismissing EDIT COLOR MAP modal
            $(document).on('click', '#color_close', function() {
                $('#colormap_table tbody').empty();
            });

            // Function called when clicking DELETE HEATMAP
            $(document).on('click', '.del_metdata', function() {
                const id = $(this).val();
                $('#id_heat').val(id);
                $('#id_delete_modal_text').text('Are you sure you want to delete ' + id + '?');
            });

            // Function called when confirming DELETE HEATMAP action
            $(document).on('click', '#delete_heatmap', function() {
                const id_heat = $('#id_heat').val();
                $.ajax({
                    url: host_trafficflowmanager + 'trafficflowmanager/api/metadata',
                    data: {
                        id: id_heat,
                        action: 'delete_metadata'
                    },
                    type: "POST",
                    async: true,
                    success: function() {
                        $('#delete_Heatmap').modal('hide');
                        alert('Successfully deleted');
                        location.reload();
                    }
                });
            });

            // Function called when clicking DELETE DATA inside HEATMAP
            $(document).on('click', '.det_data', function() {
                const id = $(this).val();
                const fluxName = $(this).data('flux');
                $('#id_layerName').val(id);
                $('#id_fluxName').val(fluxName);
                $('#id_confirm_delete_data_text').text('Are you sure do you want delete ' + id + ' from the Heatmap?');
            });

            // Function called when confirming DELETE DATA in HEATMAP action
            $(document).on('click', '#confirmDeletedData', function() {
                const layerName = $('#id_layerName').val();
                const flux = $('#id_fluxName').val();
                $.ajax({
                    url: host_trafficflowmanager + 'trafficflowmanager/api/metadata',
                    data: {
                        id: layerName,
                        action: 'delete_data'
                    },
                    type: "POST",
                    async: true,
                    success: function() {
                        $('#data_elimination').modal('hide');
                        load_heatmap_data(flux);
                    }
                });
            });
        });

        function load_heatmap_data(flux_name) {
            $('#list_header').text('Traffic Flow Instances List: ' + flux_name);
            $('.loader').show();

            // Call API
            $.ajax({
                url: host_trafficflowmanager + 'trafficflowmanager/api/metadata',
                data: {
                    fluxName: flux_name
                },
                type: "GET",
                async: true,
                dataType: 'json',
                success: function(data) {

                    // Destroy previous table, if any
                    if ( $.fn.dataTable.isDataTable( '#value_table' ) ) {
                        $('#value_table').DataTable().destroy();
                    }

                    // Empty previous content
                    $('#value_table tbody').empty();
                    $('#data_content').empty();

                    // Hide loader
                    $('.loader').hide();

                    // Show data
                    for (let i = 0; i < data.length; i++) {
                        $('#value_table tbody').append('<tr><td>' + data[i]['dateTime'] + '</td><td>' + data[i]['duration'] + '</td><td>' + data[i]['layerName'] + '</td><td><button type="button" class="delDashBtn det_data" data-target="#data_elimination" data-toggle="modal" data-flux=' + data[i]['fluxName'] + ' value=' + data[i]['layerName'] + '>DEL</button></td></tr>');
                    }

                    // Use DataTable for paging and ordering
                    $('#value_table').DataTable({
                        "searching": false,
                        "paging": true,
                        "ordering": false,
                        "info": false,
                        "responsive": true,
                        "lengthMenu": [5, 10, 15],
                        "iDisplayLength": 10,
                        "pagingType": "full_numbers",
                        "dom": '<"pull-left"l><"pull-right"f>tip',
                        "language": {
                            "paginate": {
                                "first": "First",
                                "last": "Last",
                                "next": "Next >>",
                                "previous": "<< Prev"
                            },
                            "lengthMenu": "Show	_MENU_ ",
                        }
                    });

                    // Reload page if no more data to show
                    if (data.length === 0) {
                        location.reload();
                    }
                },
                error: function() {
                    $('.loader').hide();
                    $('#data_content').append('<div class="panel panel-default"><div class="panel-body">Error when loading data</div></div>');
                }
            });
        }

    </script>

</body>

</html>

