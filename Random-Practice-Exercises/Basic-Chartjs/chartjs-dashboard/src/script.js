/* Script.js - Shows the following charts using chart.js:
    1) Bar Chart for Grades by Subject
    2) Line Chart showing weekly performance over 6 weeks for Student A 
    3) Doughnot Chart for Grade Distribution accross subjects for Student A
*/

// Require the proper modules
import mockDB from './mock-db.js';
import Chart from 'chart.js/auto';  // Load all chart.js components

// Making a bar chart for Grades by subject
function visualizeGradeBySubject(){
    const grades = mockDB.getGrades();  // Get the data from pretend db
    const studentA = grades.datasets.find(item => item.label == 'Student A');  // Getting specific data from student

    // Create the chart
    new Chart(
        document.getElementById('barChart'),
        {
        type: 'bar',
        data: {
            labels: grades.labels,
            datasets: [{
                label: "Grades per subject",
                data: studentA.data
            }]
        }
    });
}
visualizeGradeBySubject();

// Line chart for weekly performance for student a
function visualizeWeeklyPerformance(){
    const weekData = mockDB.getWeeks();
    const mathProgressData = weekData.datasets.find(item => item.label == 'Math Progress')
0
    new Chart(
        document.getElementById('lineChart'),
        {
            type: 'line',
            data: {
                labels: weekData.labels,
                datasets: [
                    {
                        label: 'Weekly performance',
                        data: mathProgressData.data
                    }
                ]
            }
        }
    );
}
visualizeWeeklyPerformance();