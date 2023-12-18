select 1;


insert into CONSUMER_ESERVICE(agreement_id, eservice_id, consumer_id, descriptor_id, event_id, state, tmst_insert) VALUES
    ('BC-agreement', 'BC-eservice', 'BC-consumer', 'BC-descriptor', 12, 'ACTIVE', '2020-01-01 00:00:00');


insert into ESERVICE(eservice_id, producer_id, descriptor_id, event_id, state, tmst_insert) VALUES
    ('BC-eservice', 'BC-producer', 'BC-descriptor', 12, 'PUBLISHED', '2020-01-01 00:00:00');

insert into SIGNAL(id, correlation_id, signal_id,object_id, eservice_id, object_type, signal_type, tmst_insert) VALUES
    ('1234', 'BC-Correlation', 1, 'BC-obj', 'BC-eservice', 'CREATE', 'CREATE', '2020-01-01 00:00:00')