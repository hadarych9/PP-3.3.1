function showTable() {
    $.getJSON('http://localhost:8080/users', function (users) {
        let tr = [];
        for (let i = 0; i < users.length; i++) {
            let roles = [];
            let userRoles = users[i].roles;
            for (let j = 0; j < userRoles.length; j++) {
                roles.push(userRoles[j].name + '</br>');
            }
            let rolesNoComma = roles.join("");
            tr.push('<tr id="tr' + users[i].id + '">');
            tr.push('<td id="tableID">' + users[i].id + '</td>');
            tr.push('<td id="tableName">' + users[i].username + '</td>');
            tr.push('<td id="tableAge">' + users[i].age + '</td>');
            tr.push('<td id="tableRoles">' + rolesNoComma + '</td>');
            tr.push('<td><button type="button" class="btn btn-info" data-toggle="modal" data-target="#update" data-id=' + users[i].id + '>Изменить</button></td>');
            tr.push('<td><button type="button" class="btn btn-danger" data-toggle="modal" data-target="#delete" data-id=' + users[i].id + '>Удалить</button></td>');
            tr.push('</tr>');
        }
        $('#user-row').append($(tr.join('')));
    });
}

function updateTable(id){
    $.ajax({
        type: "GET",
        url: 'users/' + id,
        success: (user) => {
            let row = $('#user-row').find('#tr' + id);
            let roles = [];
            let userRoles = user.roles;
            for (let j = 0; j < userRoles.length; j++) {
                roles.push(userRoles[j].name + '</br>');
            }
            let rolesNoComma = roles.join("");
            row.find('#tableID').html(id);
            row.find('#tableName').html(user.username);
            row.find('#tableAge').html(user.age);
            row.find('#tableRoles').html(rolesNoComma);
        },
        error: (e) => {
            console.log("ERROR: ", e);
        }
    });
}

function getAllRoles(){
    let allRoles = [];
    $.ajax({
        type: 'GET',
        url: 'roles',
        async: false,
        dataType: 'json',
        success: (function (json) {
            allRoles = json;
        })
    })
    return allRoles;
}

$(document).ready(function () {
    showTable();
});

$('#submitAdd').click(function (event) {
    let name = $('#add-name').val();
    let password = $('#add-password').val();
    let age = $('#add-age').val();
    let addRoles = $('#add-roles').val();
    let allRoles = getAllRoles();
    let finalRoles = [];
    $.each(allRoles, function (index, role) {
        if($.inArray(role.role, addRoles) > -1) finalRoles.push(role);
    })
    $.ajax({
        type: 'POST',
        contentType: "application/json; charset=utf-8",
        url: 'users',
        data: JSON.stringify({
            'name': name,
            'password': password,
            'age': age,
            'roles': finalRoles,
        }),
        success: function (response) {
            console.log(response);
            $('#user-row').empty();
            showTable();
            document.getElementById('addForm').reset();
            $('.nav-tabs a[href="#nav-home"]').tab('show')
        },
    });
    event.preventDefault();
});

$('#update').on('show.bs.modal', function (event) {
    let button = $(event.relatedTarget);
    let id = button.data('id');
    $.getJSON('http://localhost:8080/roles', function (roles){
        $.ajax({
            type: "GET",
            url: 'users/' + id,
            success: (user) => {
                $('#update-id').val(id);
                $('#update-name').val(user.username);
                $('#update-age').val(user.age);
                let userRoles = user.roles;
                $('#update-roles').children().remove();
                $.each(roles, function (index, role) {
                    $('#update-roles').append('<option value ="' + role.role + '">' + role.name + '</option>');
                    $.each(userRoles, function (index, userRole) {
                        $('#update-roles option[value = ' + userRole.role + ']').prop('selected', true);
                    });
                });
            },
            error: (e) => {
                console.log("ERROR: ", e);
            }
        });
    });
});

$('#submitUpdate').click(function (event) {
    let id = $('#update-id').val();
    let name = $('#update-name').val();
    let password = $('#update-password').val();
    let age = $('#update-age').val();
    let userRoles = $('#update-roles').val();
    let allRoles = getAllRoles();
    let updateRoles = [];
    $.each(allRoles, function (index, role) {
        if($.inArray(role.role, userRoles) > -1) updateRoles.push(role);
    })
    $.ajax({
        type: 'PUT',
        contentType: "application/json; charset=utf-8",
        url: 'users',
        data: JSON.stringify({
            'id': id,
            'name': name,
            'password': password,
            'age': age,
            'roles': updateRoles,
        }),
        success: function (response) {
            console.log(response);
            updateTable(id);
            $(".modal").modal("hide");
        }
    });
    event.preventDefault();
});

$('#delete').on('show.bs.modal', function (event) {
    let button = $(event.relatedTarget);
    let id = button.data('id');
    $.ajax({
        type: "GET",
        url: 'users/' + id,
        success: (user) => {
            $('#delete-id').val(id);
            $('#delete-name').val(user.username);
            $('#delete-age').val(user.age);
            let roles = user.roles;
            $('#delete-roles').children().remove();
            $.each(roles, function (index, role) {
                $('#delete-roles').append('<option value ="' + role.role + '">' + role.name + '</option>');
            });
        },
        error: (e) => {
            console.log("ERROR: ", e);
        }
    });
});

$('#submitDelete').click(function (event) {
    let id = $('#delete-id').val();
    $.ajax({
        type: 'DELETE',
        contentType: "application/json; charset=utf-8",
        url: 'users/' + id,
        success: function (response) {
            console.log(response);
            $('#user-row').find('#tr' + id).remove();
            $(".modal").modal("hide");
        }
    });
    event.preventDefault();
});
