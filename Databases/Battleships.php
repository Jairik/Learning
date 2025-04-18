<?php session_start(); ?>
<!-- Website showcasing the search, insert, and delete functionality of the Battleship database
Live website can be found here: https://lamp.salisbury.edu/~jmccauley4/dev.php 
Developer Note: My inconsistent use of camel case and snake case is awful, I am immensely sorry-->

<!DOCTYPE html>
<html lang="en">

<!-- Developer debugging - Displays any errors on screen -->
<?php
 //error_reporting(E_ALL);
 //ini_set('display_errors', 1);
 //session_start();
 ?> 

<!-- Website title-->
<head>
    <title>JJ's Query Tool</title>
    <link rel="stylesheet" href="styles.css"> <!-- Linking styles.css file -->
    <!-- Linking custom font (Asap) -->
    <link href="https://fonts.googleapis.com/css2?family=Asap:wght@400;500;600&display=swap" rel="stylesheet">
</head>
<body>
<div class="wrapper">

<!-- Establishing a connection to the database -->
<?php
if($connection = @mysqli_connect('localhost', 'jmccauley4', 'jmccauley4', 'jmccauley4DB')){
    mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);  // Enabling exception reporting
    //print '<p>Successfully connected to MySQL.</p>';
}
else {
    print '<p>Connection to MySQL failed.</p>';
}
?>

<!-- Creating title and short description at top of the webpage -->
<div class="border-section">
 <h1 class="TitleText">JJ's Battleship Query Tool</h1>
 <p class="websiteDescription">
 Welcome to JJ's Query Tool for the battleship database! To get started, select a table from 
 the database to load it, then feel free to modify or select as you wish! Thank you!
 </p>
</div>

<!-- Dropdown menu for table in database (table should be remembered on refresh -->
<div class="border-section">
<h1 class=tableSelection>Select Table:</h1>
<form method="POST">
    <label for="db_table"></label>
    <select id="db_table" name="db_table">
        <option value="Classes" <?= ($_SESSION['selectedTable'] ?? '') == 'Classes' ? 'selected' : '' ?>>Classes</option>
        <option value="Ships" <?= ($_SESSION['selectedTable'] ?? '') == 'Ships' ? 'selected' : '' ?>>Ships</option>
        <option value="Battles" <?= ($_SESSION['selectedTable'] ?? '') == 'Battles' ? 'selected' : '' ?>>Battles</option>
        <option value="Outcomes" <?= ($_SESSION['selectedTable'] ?? '') == 'Outcomes' ? 'selected' : '' ?>>Outcomes</option>
    </select>
    <input type="Submit" value="Load Table">
</form>
</div>


<!-- Getting table name from dropdown -->
<?php
if($_SERVER["REQUEST_METHOD"] == "POST" && isset($_POST['db_table'])){
    $_SESSION['selectedTable'] = $_POST['db_table']; // Retrieving the table
    $selectedTable = $_SESSION['selectedTable'];
    $sql_query = "SELECT * FROM " . $selectedTable;
    /* Retrieve the column names of the chosen table from the database */
    $selectedTable = $_SESSION['selectedTable'];
    $sql_col_query = "SHOW COLUMNS FROM " . $selectedTable;
    $result_col_query = mysqli_query($connection, $sql_col_query);
    $table_columns = [];
    if(mysqli_num_rows($result_col_query) > 0){ 
        while($row = mysqli_fetch_assoc($result_col_query)){  // Looping through all results of the query
            $table_columns[] = htmlspecialchars($row['Field']);
        }
    }
    else{  // No results found from the query (should never happen)
        print '<p> Please choose a valid table from the database </p>';
    }
    $selectedTable = $_SESSION['selectedTable'];
}
// Setting flags to determine which queries are used
$sQuery = false;
$iQuery = false;
$dQuery = false;
// Variable used throughout the script
$selectFrom = "*";  // Default to *
?>

<!-- Defining PHP wrapper functions -->
<?php
// Wrapper function to add a dropdown with a specified array of columns
function generateDropdown($table_columns, $hasSecondaryArg = false, $secondaryArg = '', $doWhere = false, $name = 'column_name'){
    //$col_dropdown = '<form method="POST">';
    $col_dropdown = '<label for="' . $name . '">';
    if($doWhere){
        $col_dropdown .= ' WHERE: </label>';
    } 
    $col_dropdown .= '<select id="' . $name . '" name="' . $name . '">';
    // Add each column to the dropdown
    foreach ($table_columns as $col_name ){
        $col_dropdown .= '<option value="' . htmlspecialchars($col_name) . '">' . htmlspecialchars($col_name) .'</option>';
    }
    $col_dropdown .= '</select>';
    //$col_dropdown .= '<input type="submit" value="Submit Column">';
    //$col_dropdown .= '</form>';
    return $col_dropdown;
}

// Wrapper function to print an input field with a POST request (parameter being variable name)
function printInputField($inputName = 'selectWhereClause'){
    //$input_field = '<form method="POST">';
    $input_field = '<input type="text" name="' . $inputName . '">';
    //$input_field .= '</form>';
    print $input_field;
}

// Converts the user input to the correct datatype (string or number)
function formatValue($val){
    if(is_numeric($val)){
        return $val + 0;  // Forcing numeric conversion
    }
    else{
        return "'" . addslashes((string)$val) ."'";
    }
}
?>

<?php
/* Ensuring that the table is correctly loaded*/
$selectedTable = $_SESSION['selectedTable'] ?? null;
$table_columns = [];
if ($selectedTable !== null) {
    $sql_col_query = "SHOW COLUMNS FROM " . $selectedTable;
    $result_col_query = mysqli_query($connection, $sql_col_query);

    if ($result_col_query && mysqli_num_rows($result_col_query) > 0) {
        while ($row = mysqli_fetch_assoc($result_col_query)) {
            $table_columns[] = htmlspecialchars($row['Field']);
        }
    } else {
        print "<p>⚠️ Could not load columns from table: $selectedTable</p>";
    }
}
?>

<!-- Creating the SELECT section -->
<div class="border-section">
 <h1>SELECT</h1>
 <?php    
    /* Creating a dropdown menu for the different columns */
    print '<form method="POST" action="">';  // Starting the form
    // Ensure that the table properly loads in
    print '<input type="hidden" name="db_table" value="' . htmlspecialchars($_SESSION['selectedTable'] ?? '') . '">';
    // Creating the first dropdown
    $col_dropdown = '<label for="selectFrom">';
    $col_dropdown .= '<select id="selectFrom" name="selectFrom">';
    // Adding a option to select all columns
    $col_dropdown .= '<option value="*">ALL</option>';
    // Add each column to the dropdown
    foreach ($table_columns as $col_name ){
        $col_dropdown .= '<option value="' . htmlspecialchars($col_name) . '">' . htmlspecialchars($col_name) .'</option>';
    }
    $col_dropdown .= '</select>';
    print $col_dropdown;  // Print the newly created dropdown
    print "<br>";  // Skip to next line for formatting
    // Creating the second dropdown using templates
    $dropdown_html = generateDropdown($table_columns, doWhere: true);
    if(isset($dropdown_html)){
        print $dropdown_html;  // Print the dropdown
    }
    print '=';
    /* Creating a submit button that retrieves variables from the POST request */
    printInputField();
    print '<input type="submit" name="sSubmit" value="Query">';
    print '</form>';  // Ending the form
    if ($_SERVER["REQUEST_METHOD"] == "POST" && isset($_POST['sSubmit'])) {
        // Get the raw value from the whereClause field
        $rawselectWhereClause = $_POST["selectWhereClause"] ?? '';
        // Get the other selected columns from the dropdowns
        $selectedColumn = isset($_POST["column_name"]) ? $_POST["column_name"] : '*';
        $selectFrom = isset($_POST["selectFrom"]) ? $_POST["selectFrom"] : '*';
        // Begin building the SELECT statement
        $s_sql_query = "SELECT " . $selectFrom . " FROM " . $selectedTable;
        // Only process the where clause if the raw input is non-empty after trimming
        if (!empty(trim($rawselectWhereClause))) {
            // Formatting the trimmed value
            $formattedselectWhereClause = formatValue(trim($rawselectWhereClause));
            $s_sql_query .= " WHERE " . $selectedColumn . " = " . $formattedselectWhereClause;
        }
        $sQuery = true;
    }
?>
</div>

<!-- Creating the INSERT section -->
<div class="border-section">
 <h1>INSERT</h1>
 <?php
    /* Creating various columns for the different column attributes*/
    $i = 0;  // Creating index for naming inputboxes
    $inputName = '';
    $colNames = [];
    $printedInputFields = '<form method="POST">';
    $printedInputFields .= '<input type="hidden" name="db_table" value="' . htmlspecialchars($_SESSION['selectedTable'] ?? '') . '">';
    $i_sql_query = "INSERT INTO " . $selectedTable . " (";
    // Looping through each column to create text (label) and input box
    foreach($table_columns as $col_name){
        $printedInputFields .= "<td><strong>" . htmlspecialchars($col_name) . ": </strong></td>"; // Print attribute name
        $inputName = ((htmlspecialchars($col_name)) . $i);  // Generate unique name for input box
        $hint = "";  // Expansion opportunity later
        $printedInputFields .= '<input type="text" name="' . $col_name . '"  placeholder="' . $hint . '">';  // Print input box
        $colNames = $inputName;  // Add name to list for POST retrieval later
        $i++;  // Incremement index for future naming purposes
        $printedInputFields .= "</br>";
        $i_sql_query .= $col_name . ", ";
    }

    // Formatting SQL query
    $i_sql_query = rtrim($i_sql_query, ", ") . ") VALUES (";

    // Creating submit button
    $printedInputFields .= '<input type="submit" name="iSubmit" value="Insert">';
    $printedInputFields .= '</form><br>';

    // Print everything and whatnot
    print $printedInputFields;

    // Pulling all the values from the POST request for the query
    if($_SERVER["REQUEST_METHOD"] == "POST" && isset($_POST['iSubmit'])){
        // Check all the attribute input boxes to ensure that they are correctly filled
        $isMissingFields = false;  // Flag to indicate if any fields are missing
        $validFieldCount = 0;
        //NOTE: Loop through and check, changing flag if missing
        foreach($colNames as $name){
            if(isset($name) && $name != '') {
                $validFieldCount++;
            }
            else{  // Field is not valid, change flag and break
                $isMissingFields = true;
                break;
            }
        }
        // Adding the custom input values
        $insertValues = [];
        // Getting the inputs from all the insert boxes
        foreach($table_columns as $col_name){
            $insertValues[] = isset($_POST[$col_name]) ? formatValue($_POST[$col_name]) : "NULL";
        }
        $i_sql_query .= implode(", ", $insertValues) . ");"; // Adding the inputs and finishing query 
        $iQuery = true;
    }
 ?>
</div>

 <!-- Creating the DELETE section -->
<div class="border-section">
 <h1>DELETE</h1>

 <?php
    // Creating dropdown with columns to delete from
    $col_dropdown = '<strong>Where:</strong> <form method="POST"> <label for="deleteFrom">';
    $col_dropdown .= '<select id="deleteFrom" name="deleteFrom">';
    // Add each column to the dropdown
    foreach ($table_columns as $col_name){
        $col_dropdown .= '<option value="' . htmlspecialchars($col_name) . '">' . htmlspecialchars($col_name) .'</option>';
    }
    $col_dropdown .= '</select>';
    print $col_dropdown;  // Print the newly created dropdown

    // Adding input field for user to select value to delete
    print " = ";  // Formatting
    printInputField("deleteWhereClause");
    print '<input type="submit" name="dSubmit" value="Delete">';
    print '</form>';  // Ending the form

    // Building the query
    if($_SERVER["REQUEST_METHOD"] == "POST" && isset($_POST['dSubmit'])){
        $deleteWhereClause = isset($_POST["deleteWhereClause"]) ? formatValue($_POST["deleteWhereClause"]) : "NULL";
        $selectedColumn = isset($_POST["deleteFrom"]) ? $_POST["deleteFrom"] : $table_columns[0];
        // Setting the query variable
        try{
            if(!empty(trim($selectedColumn))) {
                if(!empty(trim($deleteWhereClause))) {  //If the where Clause is filled out
                    $d_sql_query = "DELETE FROM " . $selectedTable . " WHERE ". $selectedColumn . " = " . $deleteWhereClause;  // Add to query
                    $dQuery = true; 
                }
            }
        }
        catch (Exception $e){
            print "⚠️ Invalid Delete Attempt, please fill all fields and try again";
        }
        
    }
 ?>
 </div>

 <!-- Creating the Results section -->
<div class="border-section">
<h1><strong>Query Results</strong></h1>
<!-- Show the query results in a table-like format -->
<?php
    // Determining which query to set it to
    $defaultQuerySelected = false;
    if($sQuery){$sql_query = $s_sql_query;}
    else if($iQuery){$sql_query = $i_sql_query;}
    else if($dQuery){$sql_query = $d_sql_query;}
    else{$sql_query = "SELECT * FROM " . $selectedTable; $defaultQuerySelected = true;}  // Default

    // Printing table name on top
    print "<h3>$selectedTable</h3>";

    // Get and output the query result
    if(isset($sql_query)){
        //print "SQL QUERY: " . $sql_query;
        // Retreiving the query result
        try{
            $result = mysqli_query($connection, $sql_query);
        }
        catch(Exception $e){
            print "❌ SQL ERROR: " . mysqli_error($connection);
        }
        
        if($result === false){
            print "❌ Query failed: " . mysqli_error($connection);
        }
        else{
            // Begin making the table
            $tableOutput = "<div class=tableCenter>";
            $empty = false;
            $tableOutput .= "<table border='2'>";
            $tableOutput .= "<thead>";
            $tableOutput .= "<tr>";

            // Add in the rows of the table
            if($sQuery || $defaultQuerySelected){ //If using a SELECT statement
                try{
                    if(mysqli_num_rows($result) > 0){ 
                        // Add the used columns into the header
                        // Determine which rows to display
                        $displayColumns = ($selectFrom === "*") ? $table_columns : [$selectFrom];
                        foreach($displayColumns as $col_name){  //NOTE: THIS IS WHAT I MUST MODIFY FOR SELECT
                            $tableOutput .= "<td><strong>" . htmlspecialchars($col_name) . "</strong></td>"; // Assign proper name to column
                        }
                        $tableOutput .= "</tr>";
                        $tableOutput .= "</thead>";
                        // Loop through all results of the query
                        while($row = mysqli_fetch_assoc($result)){
                            $tableOutput .= "<tr>";
                            // Loop through all rows of the query
                            foreach($displayColumns as $col_name){
                                $tableOutput .= "<td>" . htmlspecialchars($row[$col_name]) . "</td>"; // Assign proper name to column
                            }
                            $tableOutput .= "</tr>";
                        }
                    }
                    else{
                        print '<p> ⚠️ No results found. </p>';
                        $empty = true;
        
                    }
                    $tableOutput .= "</table>";
        
                    if($empty == false){
                        print $tableOutput;  // Print the final table
                    }
                }
                catch(Exception $e){
                    "❌ Query failed: " . $e;
                }
            }
            else {  // Otherwise, using an INSERT or DELETE Statement
                try{
                    // Add all the column names
                    foreach($table_columns as $col_name){
                        $tableOutput .= "<td><strong>" . htmlspecialchars($col_name) . "</strong></td>"; // Assign proper name to column
                    }
                    $tableOutput .= "</tr>";
                    $tableOutput .= "</thead>";
                    $affectedRows = mysqli_affected_rows($connection);
                    if($affectedRows > 0){
                        // Displaying appropriate message based on query
                        if($dQuery) {print "<p>Deleted $affectedRows rows.</p>";}
                        else{print "<p>Inserted $affectedRows rows.</p>";}
                    } else {
                        print "<p>No rows were deleted.</p>";
                    }
                    // Running a new SELECT * query to display the updated table
                    $newResult = mysqli_query($connection, "SELECT * FROM " . $selectedTable);
                    if($newResult && mysqli_num_rows($newResult) > 0){ 
                        $tableOutput = "<table border='2'>";
                        $tableOutput .= "<thead><tr>";
                        foreach($table_columns as $col_name){
                            $tableOutput .= "<td><strong>" . htmlspecialchars($col_name) . "</strong></td>";
                        }
                        $tableOutput .= "</tr></thead>";
                        while($row = mysqli_fetch_assoc($newResult)){
                            $tableOutput .= "<tr>";
                            foreach($table_columns as $col_name){
                                $tableOutput .= "<td>" . htmlspecialchars($row[$col_name]) . "</td>";
                            }
                            $tableOutput .= "</tr>";
                        }
                        $tableOutput .= "</table>";
                        print $tableOutput;
                    } else {
                        print "<p>No rows available in table.</p>";
                    }
                }    
                catch(Exception $e){
                    "❌ Query failed: " . $e;
                }
                print "</div>";
            }
        }
        $sQuery = $iQuery = $dQuery = false;  // Setting all query types to false for next user entry
    }
?>
</div>
<br>  <!-- Formatting break -->

<p class=footer> Thank you for using JJ's Battleship Database Query Tool! </p>

</div>
</body>
</html>