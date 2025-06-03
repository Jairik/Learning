# 📊 Chart.js Practice Exercise: Student Grades Dashboard

## Objective
Build a simple dashboard using Chart.js to visualize student grades across multiple subjects and time periods.

## Files
- `index.html`
- `script.js`

## Dataset

```js
// In script.js
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

```

## Tasks
1) Bar Chart - 
Create a bar chart comparing Student A and Student B’s grades in different subjects.

2) Line Chart - 
Create a line chart showing Student A’s progress in Math over 6 weeks.

3) Doughnut Chart - 
Create a doughnut chart showing the distribution of Student A’s grades across subjects.