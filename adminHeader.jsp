<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Sidebar</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/changes.css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>

<style>
body {
    margin: 0;
    font-family: Arial, sans-serif;
}

/* Sidebar */
.sidebar {
    width: 250px;
    background-color: #333;
    color: white;
    position: fixed;
    top: 0;
    left: 0;
    height: 100%;
    overflow-y: auto;
    transition: width 0.3s;
    z-index: 1000;
}

.sidebar.collapsed {
    width: 60px;
}

.sidebar-header {
    display: flex;
    align-items: center;
    padding: 5px;
    background: #444;
}

.sidebar-header h3 {
    margin: 0;
    font-size: 18px;
    margin-left: 15px;
}

.hamburger-icon {
    background: none;
    border: none;
    color: white;
    font-size: 24px;
    cursor: pointer;
}

/* Menu */
.sidebar-menu {
    list-style: none;
    padding: 0;
    margin: 0;
}

.sidebar-menu li {
    padding: 10px;
}

.sidebar-menu li a {
    color: white;
    text-decoration: none;
    display: flex;
    align-items: center;
    font-size: 16px;
}

.sidebar-menu li a:hover {
    background-color: #575757;
}

.glyphicon {
    margin-right: 12px;
}

.menu-text {
    transition: opacity 0.3s;
}

.sidebar.collapsed .menu-text {
    opacity: 0;
}

/* Dropdown */
.dropdown-menu {
    background-color: #444;
    display: none;
    list-style: none;
    padding-left: 20px;
}

.dropdown-menu.show {
    display: block;
}

/* Content */
.content {
    margin-left: 250px;
    padding: 20px;
    transition: margin-left 0.3s;
}

.content.sidebar-collapsed {
    margin-left: 80px;
}

.first {
    color: white;
    text-decoration: none;
    font-size: larger;
}
</style>
</head>

<body>

<div class="sidebar">
    <div class="sidebar-header">
        <button class="hamburger-icon">&#9776;</button>
        <h3><a href="adminHomeView.jsp" class="first">NamasteMart</a></h3>
    </div>

    <ul class="sidebar-menu">
        <li>
            <a href="adminViewProduct.jsp">
                <span class="glyphicon glyphicon-home"></span>
                <span class="menu-text">Products</span>
            </a>
        </li>

        <li class="dropdown">
            <a href="#" class="dropdown-toggle">
                <span class="glyphicon glyphicon-th-list"></span>
                <span class="menu-text">Category <span class="caret"></span></span>
            </a>
            <ul class="dropdown-menu">
                <li><a href="adminViewProduct.jsp?type=electronics,appliance">Electronics and Appliances</a></li>
                <li><a href="adminViewProduct.jsp?type=fashion">Fashion and Apparel</a></li>
                <li><a href="adminViewProduct.jsp?type=home,kitchen">Home and Kitchen</a></li>
                <li><a href="adminViewProduct.jsp?type=outdoor,fitness">Fitness and Outdoor</a></li>
                <li><a href="adminViewProduct.jsp?type=sports">Sports and Recreation</a></li>
                <li><a href="adminViewProduct.jsp?type=toys,games">Toys and Games</a></li>
                <li><a href="adminViewProduct.jsp?type=supplements,personal_care">Health and Personal Care</a></li>
                <li><a href="adminViewProduct.jsp?type=office,stationery">Office and School Supplies</a></li>
            </ul>
        </li>

        <li>
            <a href="adminStock.jsp">
                <span class="glyphicon glyphicon-stats"></span>
                <span class="menu-text">Stock</span>
            </a>
        </li>

        <li>
            <a href="shippedItems.jsp">
                <span class="glyphicon glyphicon-send"></span>
                <span class="menu-text">Shipped</span>
            </a>
        </li>

        <li>
            <a href="unshippedItems.jsp">
                <span class="glyphicon glyphicon-list-alt"></span>
                <span class="menu-text">Orders</span>
            </a>
        </li>

        <li class="dropdown">
            <a href="#" class="dropdown-toggle">
                <span class="glyphicon glyphicon-pencil"></span>
                <span class="menu-text">Update Items <span class="caret"></span></span>
            </a>
            <ul class="dropdown-menu">
                <li><a href="addProduct.jsp">Add Product</a></li>
                <li><a href="removeProduct.jsp">Remove Product</a></li>
                <li><a href="updateProductById.jsp">Update Product</a></li>
            </ul>
        </li>

        <li>
            <a href="adminAction.jsp">
                <span class="glyphicon glyphicon-user"></span>
                <span class="menu-text">User Control</span>
            </a>
        </li>

        <li>
            <a href="adminTrans.jsp">
                <span class="glyphicon glyphicon-credit-card"></span>
                <span class="menu-text">Transactions</span>
            </a>
        </li>

        <li>
            <a href="/LogoutSrv">
                <span class="glyphicon glyphicon-log-out"></span>
                <span class="menu-text">Logout</span>
            </a>
        </li>
    </ul>
</div>

<div class="content">
    <!-- Page content here -->
</div>

<script>
document.querySelector('.hamburger-icon').addEventListener('click', function () {
    document.querySelector('.sidebar').classList.toggle('collapsed');
    document.querySelector('.content').classList.toggle('sidebar-collapsed');
});

$('.dropdown-toggle').click(function (e) {
    e.preventDefault();
    var menu = $(this).next('.dropdown-menu');
    $('.dropdown-menu').not(menu).removeClass('show');
    menu.toggleClass('show');
});
</script>

</body>
</html>
