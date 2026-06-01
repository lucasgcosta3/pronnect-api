ALTER TABLE project_bid ADD COLUMN proposal_id UUID REFERENCES proposal(id);
