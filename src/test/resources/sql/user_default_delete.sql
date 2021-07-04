delete
from access_card_has_roles
where access_card_id in ('1', '153', '154', '200', '201');
delete
from cars
where id in ('200');
delete
from users
where access_card_id in ('1', '153', '154', '200', '201');
delete
from access_card
where id in ('1', '153', '154', '200', '201');
