package de.c0debase.bot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import de.c0debase.bot.database.Database;
import de.c0debase.bot.database.model.User;
import de.c0debase.bot.database.mongodb.MongoDatabase;

public class MongoTest {

    private Database database;

    @Before
    public void setup(){
        database = new MongoDatabase("localhost", 27017);
    }

    @Test
    public void testUserCreationWithGetter(){
        final User user = database.getUserDao().getOrCreateUser("123456789", "987654321");
        assertEquals(user.getLevel(), 0);
        assertEquals(user.getXp(), 0);
    }

    @Test
    public void testXpAddition(){
        final User user = database.getUserDao().getUser("123456789", "987654321");
        assertNotNull(user);
        user.addXP(100);
        database.getUserDao().updateUser(user);
        assertNotEquals(0, database.getUserDao().getUser("123456789", "987654321"));
    }

    @Test
    public void testDeletion(){
        database.getUserDao().deleteUser("123456789", "987654321");
        assertNull(database.getUserDao().getUser("123456789", "987654321"));
    }

}