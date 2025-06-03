/* Script.js - Shows the following charts using chart.js:
    1) Bar Chart for Grades by Subject
    2) Line Chart showing weekly performance over 6 weeks for Student A 
    3) Doughnot Chart for Grade Distribution accross subjects for Student A
*/

// Require the proper modules
const mockDB = require('./mock-db.js');
import Chart from 'chart.js/auto';

// Making a bar chart for Grades by subject
function visualizeGradeBySubject(){
    const data = mockDB.getGrades();  // Get the data from pretend db

    // Create the chart
    new Chart(
        document.getElementById('barChart'),
        {
        type: 'bar',
        data: {
            labels: data.map(row => row.label),
            datasets: [{
                label: "Grades per subject",
                data: data.map(row => row.grade)
            }]
        }
    });
}

// Line chart for weekly performance for student a
function visualizeWeeklyPerformance(){
    const data = mockDB.getWeeks();

    new Chart(
        document.getElementById('lineChart'),
        {
            type: 'line',
            data: {
                labels: data.map(row => row.labels),
                datasets: [
                    {
                        label: 'Student A weekly performance',
                        data: data.map(row => row.data)
                    }
                ]
            }
        }
    );
}

visualizeGradeBySubject();
visualizeWeeklyPerformance();