const API_URL = 'http://localhost:8080/students'; 


function getAllStudents() {
    fetch(API_URL)
        .then(response => response.json())
        .then(data => {
            const studentTable = document.getElementById('studentTable');
            studentTable.innerHTML = '';
            data.forEach(student => {
                const row = `<tr>
                    <td>${student.rollNo}</td>
                    <td>${student.name}</td>
                    <td>${student.age}</td>
                    <td>${student.branch}</td>
                    <td>
                        <button onclick="updateStudent(${student.rollNo})">Update</button>
                        <button onclick="deleteStudent(${student.rollNo})">Delete</button>
                    </td>
                </tr>`;
                studentTable.innerHTML += row;
            });
        })
        .catch(error => console.error('Error:', error));
}


function addStudent() {
	
    const name = document.getElementById('name').value;
    const age = document.getElementById('age').value;
    const branch = document.getElementById('branch').value;

    const student = { name, age, branch };

    fetch(`${API_URL}/add`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(student)
    })
        .then(response => response.json())
        .then(data => {
            console.log('Success:', data);
            getAllStudents(); 
        })
        .catch(error => console.error('Error:', error));
}


function deleteStudent(id) {
    fetch(`${API_URL}/delete/${id}`, {
        method: 'DELETE'
    })
        .then(response => {
            console.log('Deleted:', id);
            getAllStudents();
        })
        .catch(error => console.error('Error:', error));
}

function updateStudent(id){
	fetch(`${API_URL}/update/${id}`,{
		method:'PUT'
	})
	.then(response=> {
		console.log("Updated:",id);
		getAllStudents();
	})
	.catch(error=>console.error("Error:",error));
}

getAllStudents();
