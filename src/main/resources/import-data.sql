INSERT INTO public.role (name) VALUES ('ROLE_USER');
INSERT INTO public.role (name) VALUES ('ROLE_ADMIN');

INSERT INTO public.users (age, email, name, password) VALUES (15, 'user@user.ru', 'user', '$2a$12$r/UgXbidDftGfwuZlXVd4eECHJcp1bviAVvcsQPAp0HVtwEBFU3sq');
INSERT INTO public.users (age, email, name, password) VALUES (20, 'admin@admin.ru', 'admin', '$2a$12$VtePqSbEBG0gNKa99GxTJOZxqetVIvuf/vKxHkk7tblicWhLljW5K');


INSERT INTO public.users_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO public.users_roles (user_id, role_id) VALUES (2, 1);
INSERT INTO public.users_roles (user_id, role_id) VALUES (2, 2);
