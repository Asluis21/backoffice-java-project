package lab.db_studio.rest.util;

import lab.db_studio.rest.HttpStatus;

public class HttpListStatus {
    
    public static final HttpStatus WRONG_METHOD = new HttpStatus(405, "Wrong method");
    public static final HttpStatus ENDPOINT_NOT_FOUND = new HttpStatus(404, "Not found endpoint");
    public static final HttpStatus OK = new HttpStatus(200, "Ok");
    public static final HttpStatus INTERNAL_SERVER_ERROR = new HttpStatus(500, "INTERNAL_SERVER_ERROR");
}
