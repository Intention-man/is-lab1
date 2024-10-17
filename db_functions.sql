CREATE OR REPLACE FUNCTION calculate_total_age()
    RETURNS BIGINT AS $$
DECLARE
    total_age BIGINT;
BEGIN
    SELECT SUM(age) INTO total_age FROM dragon;
    RETURN total_age;
END;
$$ LANGUAGE plpgsql;

