<!DOCTYPE html>

<?php
$url_api='http://192.168.1.110:8080/trafficflowmanager/api/metadata';
$json_api = file_get_contents($url_api);
$list_api = json_decode($json_api);
$count = count($list_api);
?>

<html lang="en">

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
                                    <th><div><a>Unit of Measure</a></div></th>
                                    <th><div><a>ColorMap</a></div></th>
                                    <th><div><a>Static Graph Name</a></div></th>
                                </tr>
                            </thead>

                            <!-- Rows -->
                            <tbody>
                            <?php
                            for ($i = 0; $i < $count; $i++) {
                                echo("<tr>");
                                echo("<td>" . $list_api[$i]->fluxName . "</td>");
                                echo("<td>" . $list_api[$i]->locality . "</td>");
                                echo("<td>" . $list_api[$i]->organization . "</td>");
                                echo("<td>" . $list_api[$i]->scenarioID . "</td>");
                                echo("<td>" . $list_api[$i]->instances . "</td>");
                                echo("<td><button type='button' class='viewDashBtn viewList' data-target='#view-modal' data-toggle='modal' value='".$list_api[$i]->fluxName."'>VIEW</button></td>");
                                echo("<td>" . $list_api[$i]->metricName . "</td>");
                                echo("<td>" . $list_api[$i]->unitOfMeasure . "</td>");
                                echo("<td>" . $list_api[$i]->colorMap . "</td>");
                                echo("<td>" . $list_api[$i]->staticGraphName . "</td>");
                                echo("</tr>");
                            }
                            ?>
                            </tbody>
                        </table>
                    </div>

                    <!-- View Modal -->
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
                    <!-- -->
                </div>
            </div>
        </div>
    </div>

    <!-- JavaScript -->
    <script type='text/javascript'>
        $(document).ready(function() {
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
                $('#list_header').text('Traffic Flow Instances List: ' + flux_name);
                $('.loader').show();

                // Call API
                $.ajax({
                    url: 'http://192.168.1.110:8080/trafficflowmanager/api/metadata',
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
                        if (data.length > 0) {
                            for (let i = 0; i < data.length; i++) {
                                $('#value_table tbody').append('<tr><td>' + data[i]['dateTime'] + '</td><td>' + data[i]['duration'] + '</td><td>' + data[i]['layerName'] + '</td><td><button type="button" class="delDashBtn det_data" data-target="#data_elimination" data-toggle="modal">DEL</button></td></tr>');
                            }
                        } else {
                            $('#data_content').append('<div class="panel panel-default"><div class="panel-body">There is no data</div></div>');
                        }

                        // Use DataTable for paging and ordering
                        $('#value_table').DataTable({
                            "searching": false,
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
                    },
                    error: function() {
                        $('.loader').hide();
                        $('#data_content').append('<div class="panel panel-default"><div class="panel-body">Error when loading data</div></div>');
                    }

                });
            });
        });

    </script>
</body>
</html>

