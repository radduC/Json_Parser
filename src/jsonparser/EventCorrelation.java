package jsonparser;

import java.util.List;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

public class EventCorrelation {
    private final JSON json;

    public EventCorrelation(JSON json) {
        this.json = json;
    }

    public JSON getJson() {
        return json;
    }

    public String get(String[] array) {
        List<HashMap<String, Object>> jsonArray = null;
        HashMap<String, Object> jsonObject = null;
        String jsonString = null;
        String objectClass;
        Deque<Object> itemsStack = new LinkedList<>();
        Deque<String> previousStrings = new LinkedList<>();

        itemsStack.push(json.getJson());

        for (String string : array) {
            objectClass = itemsStack.peek().getClass().toString();

            if (string.matches("\\d+")) {
                try {
                    jsonObject = ((List<HashMap<String, Object>>) itemsStack.peek()).get(Integer.parseInt(string));
                    itemsStack.push(jsonObject);

                } catch (IndexOutOfBoundsException e) {
                    return ("GET_ERROR_INDEX_OUT_OF_RANGE " + "\"" + previousStrings.peek() + "\"");

                } catch (Exception e) {
                    return ("GET_ERROR_NOT_ARRAY " + "\"" + previousStrings.peek() + "\"");
                }

            } else {
                String valueClass;

                try {
                    jsonObject = (HashMap<String, Object>) itemsStack.peek();
                    valueClass = jsonObject.get(string).getClass().toString();

                    if (valueClass.contains("String")) {
                        jsonString = (String) jsonObject.get(string);
                        itemsStack.push(jsonString);

                    } else if (valueClass.contains("List")) {
                        jsonArray = (List<HashMap<String, Object>>) jsonObject.get(string);
                        itemsStack.push(jsonArray);

                    } else if (valueClass.contains("HashMap")) {
                        jsonObject = (HashMap<String, Object>) jsonObject.get(string);
                        itemsStack.push(jsonObject);
                    }

                } catch (NullPointerException e) {
                    return "GET_KEY_NOT_FOUND " + (itemsStack.size() == 1 ? "root" : "\"" + string + "\"");

                } catch (ClassCastException e) {
                    return ("GET_ERROR_NOT_OBJECT "
                            + (itemsStack.size() == 1 ? "root" : "\"" + previousStrings.peek() + "\""));
                }

            }

            previousStrings.push(string);
        }

        Object object = itemsStack.peek();
        objectClass = object.getClass().toString();

        if (objectClass.contains("String")) {
            String result = (String) object;

            if (result.matches("[\btrue\b|\bfalse\b|\\d]+")) {
                return result;
            }

            return "\"" + result + "\"";

        } else if (objectClass.contains("List")) {
            return "JSON_ARRAY";

        } else if (objectClass.contains("HashMap")) {
            return "JSON_OBJECT";
        }

        return "";
    }

    public String put(String[] array) {
        Deque<Object> itemsStack = new LinkedList<>();
        Deque<String> previousStrings = new LinkedList<>();
        itemsStack.push(json.getJson());

        String key = array[array.length - 2];
        String value = array[array.length - 1];
        array = Arrays.copyOf(array, array.length - 2);

        List<HashMap<String, Object>> jsonArray = null;
        HashMap<String, Object> jsonObject = null;
        String jsonString = null;
        String objectClass;

        for (String string : array) {
            objectClass = itemsStack.peek().getClass().toString();

            if (string.matches("\\d+")) {
                try {
                    jsonObject = ((List<HashMap<String, Object>>) itemsStack.peek()).get(Integer.parseInt(string));
                    itemsStack.push(jsonObject);

                } catch (IndexOutOfBoundsException e) {
                    return ("PUT_ERROR_INDEX_OUT_OF_RANGE " + "\"" + previousStrings.peek() + "\"");

                } catch (Exception e) {
                    return ("PUT_ERROR_NOT_ARRAY " + "\"" + previousStrings.peek() + "\"");
                }

            } else {
                String valueClass;

                try {
                    jsonObject = (HashMap<String, Object>) itemsStack.peek();
                    valueClass = jsonObject.get(string).getClass().toString();

                    if (valueClass.contains("String")) {
                        jsonString = (String) jsonObject.get(string);
                        itemsStack.push(jsonString);

                    } else if (valueClass.contains("List")) {
                        jsonArray = (List<HashMap<String, Object>>) jsonObject.get(string);
                        itemsStack.push(jsonArray);

                    } else if (valueClass.contains("HashMap")) {
                        jsonObject = (HashMap<String, Object>) jsonObject.get(string);
                        itemsStack.push(jsonObject);
                    }

                } catch (NullPointerException e) {
                    return "PUT_KEY_NOT_FOUND " + "\"" + string + "\"";

                } catch (ClassCastException e) {
                    return ("PUT_ERROR_NOT_OBJECT "
                            + (itemsStack.size() == 1 ? "root" : "\"" + previousStrings.peek() + "\""));
                }

            }

            previousStrings.push(string);
        }

        Object item = itemsStack.peek();
        objectClass = item.getClass().toString();

        if (objectClass.contains("String")) {
            return "PUT_ERROR_NOT_OBJECT "
                    + (itemsStack.isEmpty() || itemsStack.size() == 1 ? "root" : "\"" + previousStrings.peek() + "\"");

        } else if (objectClass.contains("List")) {
            return (key.matches("\\d+") ? "PUT_ERROR_NOT_ARRAY " : "PUT_ERROR_NOT_OBJECT ")
                    + (itemsStack.isEmpty() || itemsStack.size() == 1 ? "root" : "\"" + previousStrings.peek() + "\"");

        } else if (objectClass.contains("HashMap")) {
            HashMap<String, Object> object = (HashMap<String, Object>) item;
            object.put(key, value);
        }

        return "";
    }

    public String del(String[] array) {
        Deque<Object> itemsStack = new LinkedList<>();
        Deque<String> previousStrings = new LinkedList<>();
        itemsStack.push(json.getJson());

        List<HashMap<String, Object>> jsonArray = null;
        HashMap<String, Object> jsonObject = null;
        String jsonString = null;

        for (String string : array) {
            if (string.matches("\\d+")) {
                try {
                    jsonObject = ((List<HashMap<String, Object>>) itemsStack.peek()).get(Integer.parseInt(string));
                    itemsStack.push(jsonObject);
                } catch (IndexOutOfBoundsException e) {
                    return ("DEL_ERROR_INDEX_OUT_OF_RANGE " + (itemsStack.size() == 1 ? "root" : "\"" + string + "\""));

                } catch (Exception e) {
                    return ("DEL_ERROR_NOT_ARRAY " + "\"" + previousStrings.peek() + "\"");
                }

            } else {
                String valueClass;

                try {
                    jsonObject = (HashMap<String, Object>) itemsStack.peek();
                    valueClass = jsonObject.get(string).getClass().toString();

                    if (valueClass.contains("String")) {
                        jsonString = (String) jsonObject.get(string);
                        itemsStack.push(jsonString);
                    } else if (valueClass.contains("List")) {
                        jsonArray = (List<HashMap<String, Object>>) jsonObject.get(string);
                        itemsStack.push(jsonArray);
                    } else if (valueClass.contains("HashMap")) {
                        jsonObject = (HashMap<String, Object>) jsonObject.get(string);
                        itemsStack.push(jsonObject);
                    }

                } catch (NullPointerException e) {
                    return "DEL_KEY_NOT_FOUND " + "\"" + string + "\"";

                } catch (ClassCastException e) {
                    return ("DEL_ERROR_NOT_OBJECT "
                            + (itemsStack.size() == 1 ? "root" : "\"" + previousStrings.peek() + "\""));
                }

            }

            previousStrings.push(string);
        }

        String key = previousStrings.peek();
        itemsStack.pop();
        Object item = itemsStack.pop();

        try {
            if (key.matches("\\d+")) {
                jsonArray = (List<HashMap<String, Object>>) item;
                jsonArray.remove(Integer.parseInt(key));

            } else {
                jsonObject = (HashMap<String, Object>) item;
                jsonObject.remove(key);
            }

            return "";

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}