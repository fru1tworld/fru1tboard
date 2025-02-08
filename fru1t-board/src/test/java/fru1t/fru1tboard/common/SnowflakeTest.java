package fru1t.fru1tboard.common;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class SnowflakeTest {
    Snowflake snowflake = new Snowflake();

    @Test
    void create(){
        Long id = snowflake.nextId();
        System.out.println("id = " + id);
    }

}