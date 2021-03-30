<!DOCTYPE html>

<?php
$url_api='http://192.168.1.110:8080/trafficflowmanager/api/layers';
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

    #trafficflow_table {
        margin-bottom: 0;
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
                                    <th><div><a>Date</a></div></th>
                                    <th><div><a>Duration</a></div></th>
                                    <th><div><a>Metric</a></div></th>
                                    <th><div><a>Unit of Measure</a></div></th>
                                    <th><div><a>ColorMap</a></div></th>
                                    <th><div><a>Static Graph Name</a></div></th>
                                    <th><div><a>Delete</a></div></th>
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
                                echo("<td>" . $list_api[$i]->dateTime . "</td>");
                                echo("<td>" . $list_api[$i]->duration . "</td>");
                                echo("<td>" . $list_api[$i]->metricName . "</td>");
                                echo("<td>" . $list_api[$i]->unitOfMeasure . "</td>");
                                echo("<td>" . $list_api[$i]->colorMap . "</td>");
                                echo("<td>" . $list_api[$i]->staticGraphName . "</td>");
                                echo("<td><button type='button' class='delDashBtn del_metdata' data-target='#delete-modal' data-toggle='modal' value='". $list_api[$i]->fluxName ."'>DEL</button></td>");
                                echo("</tr>");
                            }
                            ?>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

</body>
</html>

