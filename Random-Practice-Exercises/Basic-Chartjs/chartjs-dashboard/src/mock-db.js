// Mock database to return the data

const studentGrades = {
  labels: ['Math', 'Science', 'English', 'History', 'Art'],
  datasets: [
    {
      label: 'Student A',
      data: [85, 92, 78, 88, 94],
      backgroundColor: 'rgba(54, 162, 235, 0.6)',
    },
    {
      label: 'Student B',
      data: [80, 75, 90, 70, 85],
      backgroundColor: 'rgba(255, 99, 132, 0.6)',
    },
  ],
};

const weekData = {
  labels: ['Week 1', 'Week 2', 'Week 3', 'Week 4', 'Week 5', 'Week 6'],
  datasets: [{
    label: 'Math Progress',
    data: [70, 72, 75, 80, 85, 88],
    fill: false,
    borderColor: 'rgba(75, 192, 192, 1)',
    tension: 0.3
  }]
};

function getGrades(){
    return studentGrades;
}

function getWeeks(){
    return weekData;
}

// Exporting the functions
export default {
    getGrades,
    getWeeks
};
