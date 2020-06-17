package com.blueeaglecreditunion.script;

public class SqlQueries {
    public static String memberInfo() {
        return " WITH CurrentAddress(personSerial,street,additionalAddressLine,city,state,postalCode,country,combinedAddress) AS\n" +
                "    (\n" +
                " SELECT\n" +
                "     PERSON_SERIAL,\n" +
                "     STREET,\n" +
                "     ADDITIONAL_ADDRESS_LINE,\n" +
                "     CITY,\n" +
                "     STATE,\n" +
                "     POSTAL_CODE,\n" +
                "     COUNTRY,\n" +
                "     COMBINED_ADDRESS\n" +
                " FROM\n" +
                "     (\n" +
                "         SELECT\n" +
                "             PERSON.SERIAL AS PERSON_SERIAL,\n" +
                "             COALESCE(PAL.ORDINAL, 0) AS PAL_ORDINAL,\n" +
                "             MIN(COALESCE(PAL.ORDINAL, 0)) OVER (PARTITION BY PERSON.SERIAL) AS MIN_PAL_ORDINAL,\n" +
                "             COALESCE(ADDRESS.ADDITIONAL_ADDRESS_LINE, '') AS ADDITIONAL_ADDRESS_LINE,\n" +
                "             COALESCE(ADDRESS.STREET, '') AS STREET,\n" +
                "             COALESCE(ADDRESS.CITY, '') AS CITY,\n" +
                "             COALESCE(ADDRESS.STATE, '') AS STATE,\n" +
                "             COALESCE(ADDRESS.POSTAL_CODE, '') AS POSTAL_CODE,\n" +
                "             COALESCE(ADDRESS.COUNTRY, '') AS COUNTRY,\n" +
                "             COALESCE(ADDRESS.STREET || ' ', '') || COALESCE(ADDRESS.ADDITIONAL_ADDRESS_LINE || ', ', '') ||\n" +
                "             COALESCE(ADDRESS.CITY || ' ', '') || COALESCE(ADDRESS.STATE, '') || COALESCE(', ' || ADDRESS.POSTAL_CODE, '') AS COMBINED_ADDRESS\n" +
                "         FROM\n" +
                "             CORE.PERSON AS PERSON\n" +
                "             INNER JOIN\n" +
                "                CORE.ENV AS ENV ON\n" +
                "                ENV.SERIAL > 0\n" +
                "             LEFT OUTER JOIN\n" +
                "             (CORE.PERSON_ADDRESS_LINK AS PAL\n" +
                "              INNER JOIN\n" +
                "                 CORE.ADDRESS AS ADDRESS ON\n" +
                "                 PAL.ADDRESS_SERIAL = ADDRESS.SERIAL) ON\n" +
                "                         PAL.PARENT_SERIAL = PERSON.SERIAL AND\n" +
                "                         COALESCE(PAL.EFFECTIVE_DATE, '1800-01-01') <= ENV.POSTING_DATE AND\n" +
                "                         COALESCE(PAL.EXPIRATION_DATE, '2999-12-31') >= ENV.POSTING_DATE AND\n" +
                "                         PAL.BAD_ADDRESS <> 'Y' AND\n" +
                "                         PAL.CATEGORY <> 'P'   /* Previous */ -- this is not even correct.\n" +
                "     ) a\n" +
                "    WHERE\n" +
                "        PAL_ORDINAL = MIN_PAL_ORDINAL\n" +
                ")\n" +
                "\n" +
                " SELECT\n" +
                "    PERSON.LAST_NAME || COALESCE(','||PERSON.FIRST_NAME, '') || COALESCE(' '|| PERSON.MIDDLE_NAME, '') AS MEMBER_NAME,\n" +
                "    CurrentAddress.street STREET,\n" +
                "    CurrentAddress.city CITY,\n" +
                "    CurrentAddress.state STATE,\n" +
                "    CurrentAddress.postalCode ZIP,\n" +
                "    PC.VALUE EMAIL\n" +
                "\n" +
                " FROM\n" +
                "    CORE.PERSON AS PERSON\n" +
                "    INNER JOIN\n" +
                "        CORE.PERSON_CONTACT PC ON PERSON.SERIAL = PC.PARENT_SERIAL\n" +
                "    INNER JOIN\n" +
                "        CurrentAddress ON CurrentAddress.personSerial = PERSON.SERIAL\n" +
                " WHERE\n" +
                "    UPPER(PC.VALUE) LIKE ?";
    }
}
