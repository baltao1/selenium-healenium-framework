// Login behaviour shared by both pages. The click handler is bound via a
// tag/class selector (not the id), so changing the button's id breaks the
// Selenium By.id locator WITHOUT breaking the page — the exact situation
// self-healing is meant to recover from.
document.querySelector("button.login").addEventListener("click", function (e) {
  e.preventDefault();
  var user = document.getElementById("username").value;
  var pass = document.getElementById("password").value;
  var msg = document.getElementById("message");
  if (user === "admin" && pass === "secret") {
    msg.textContent = "Welcome, admin!";
  } else {
    msg.textContent = "Invalid credentials";
  }
});
