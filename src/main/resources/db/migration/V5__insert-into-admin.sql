-- ## Password: 'admin@123456' ##
insert into users(username, password) values('hackathon@admin.com', '$2a$12$kY57HmF60EzSWcRcsu/wIOPBVPMFqB8/2hPKOCEYr.m3caT8Cb6VW');
insert into users_roles(user_id, role_id) values(
    (select id from users where username='hackathon@admin.com'),
    (select id from roles where name='ROLE_ADMIN')
);