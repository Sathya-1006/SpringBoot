// Shared script for login, register, and todos pages
const SERVER_URL = "http://localhost:8080";
const token = localStorage.getItem("token");

// Login page logic
function login() {
    //to fetch email & password from user input in register.html
    const  email = document.getElementById("email").value;
    const  password = document.getElementById("password").value;


    //API call - fetching data from backend
    fetch(`${SERVER_URL}/auth/login`,{ //endpoint
        method: "POST", //http Method, required to fetch the req
        headers: {"Content-Type":"application/json"},
        body: JSON.stringify({email,password})

}) 
.then(response => {
    if (!response.ok) {
        return response.json().then(err => {
            throw new Error(err.message || "Login failed");
        });
    }

    return response.json();
})
.then(data =>{
    localStorage.setItem("token", data.token);
    window.location.href="todos.html"; //after login -> go to Todos page
})
.catch(error =>{
    alert(error.message);
})

}

// Register page logic
function register() {
    //to fetch email & password from user input in register.html
    const  email = document.getElementById("email").value;
    const  password = document.getElementById("password").value;


    //API call - fetching data from backend
    fetch(`${SERVER_URL}/auth/register`,{ //endpoint
        method: "POST", //http Method, required to fetch the req
        headers: {"Content-Type":"application/json"},
        body: JSON.stringify({email,password})

}) 
.then(response => {
    if(response.ok){
        alert("Registration Successful, Please Login!");
        window.location.href="login.html" //directing user to login page
        
    }else{
        return response.json().then(data => {throw new Error(data.message || "Registration failed")});

    }
})
.catch(error =>{
    alert(error.message);
})

}

// Todos page logic
function createTodoCard(todo) {
    //whenever the new todo is created, this function will be called

    const card = document.createElement("div");
    card.className = "todo-card";

    const checkbox = document.createElement("input");
    checkbox.type = "checkbox"
    checkbox.checked = todo.isComplete;
    checkbox.addEventListener("change", function () {
        const updatedTodo = { ...todo, isComplete: checkbox.checked };
        updateTodoStatus(updatedTodo);
    });



    const span = document.createElement("span");
    span.textContent=todo.title;

    if(todo.isComplete){
        span.style.textDecoration = "line-through";
         span.style.color = "#aaa";
    }
    const deleteBtn = document.createElement("button");
    deleteBtn.textContent = "X";
    deleteBtn.addEventListener("click", function () {
        deleteTodo(todo.id);
    });

    card.appendChild(checkbox);
    card.appendChild(span);
    card.appendChild(deleteBtn);

    return card;

}

function loadTodos() {
    //if no token
    if(!token){

        //if non token - means not authorized - so directing user to login page
        alert("Please Login first");
        window.location.href="login.html";
        return;

    }

    fetch(`${SERVER_URL}/todo`,{ 
        method: "GET",
        headers: {
            
            Authorization: `Bearer ${token}`
        },
        
}) 
.then(response => {
    if (!response.ok) {
        return response.json().then(err => {
            throw new Error(err.message || "Failed to get todos");
        });
    }

    return response.json();
})
.then((todos) => {
    const todoList = document.getElementById("todo-list");
    todoList.innerHTML=""; //throw all the old todos

    //if no todos found
    if(!todos || todos.length === 0){
        todoList.innerHTML = `<p id="empty-message">No Todos yet. Add one below!</p>`;
    }

    //if todo present
    else{
        todos.forEach(todo =>{
            todoList.appendChild(createTodoCard(todo));
        });
    }
})
.catch(error =>{
    document.getElementById("todo-list").innerHTML=`<p style="color:red">Failed to load Todos!</p>`
})



}

function addTodo() {
    const input =document.getElementById("new-todo");
    const todoText = input.value.trim(); //if the value has extra space or something -> trim it

    //API call - fetching data from backend
    fetch(`${SERVER_URL}/todo/add`,{ //endpoint
        method: "POST", //http Method, required to fetch the req
        headers: {
            "Content-Type":"application/json",
            Authorization: `Bearer ${token}`
        },
        // todo's title: value inside todoText, by default -> task marked as not completed
        body: JSON.stringify({title: todoText, description: "", isComplete: false})

}) 
.then(response => {
    if (!response.ok) {
                return response.json().then(err => {
                    throw new Error(err.message || "Failed to add todo");
                });
            }
    return response.json();
})
.then((newTodo) => {
    input.value = "";

    loadTodos();
})
.catch(error =>{
    alert(error.message);
})


}

function updateTodoStatus(todo) {
    fetch(`${SERVER_URL}/todo/${todo.id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`
        },
        body: JSON.stringify({
            id: todo.id,
            title: todo.title,
            description: todo.description,
            isComplete: todo.isComplete   // not isCompleted
        })

    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(err => {
                throw new Error(err.message || "Failed to update todo");
            });
        }
        return response.json();
    })
    .then(() => loadTodos())
    .catch(error => {
        alert(error.message);
    });
}


function deleteTodo(id) {
    //API call - fetching data from backend
    fetch(`${SERVER_URL}/todo/${id}`,{ //endpoint
        method: "DELETE", //http Method, required to fetch the req
        headers: {Authorization: `Bearer ${token}`},
}) 
.then(response => {
    if (!response.ok) {
        return response.json().then(err => {
            throw new Error(err.message || "Failed to delete todo");
        });
    }

    return response.text();
})
.then(() => loadTodos())
.catch(error =>{
    alert(error.message);
})

}

// Page-specific initializations
document.addEventListener("DOMContentLoaded", function () {
    if (document.getElementById("todo-list")) {
        loadTodos();
    }
});