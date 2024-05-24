INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version,match_rule,sub_match_rule)
VALUES
    ('f52e0794-8f19-4e2d-b6fb-c92ed8ec63f8', 'RG-CT5302C-G4', 'VOI', 'product', true, now(), now(),0, 'RG-CT5',null),
    ('d480a6c3-ef09-4704-884c-1240347eec53', 'RG-CT5502C-G4', 'VOI', 'product', true, now(), now(),0, 'RG-CT5',null),
    ('a3cbb789-32aa-430c-b69a-11c4c6a9a938', 'RG-CT5702C-G4', 'VOI', 'product', true, now(), now(),0, 'RG-CT5',null),
    ('3619771c-e078-207c-fd5d-6310423f64de', 'RG-CT5500C-CS', 'VOI', 'product', true, now(), now(),0, 'RG-CT5',null)
    ON CONFLICT(id) DO NOTHING;