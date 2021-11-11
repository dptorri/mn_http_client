#!/bin/bash

curl -X POST "http://localhost:8100/persons" \
-H "Accept: application/json" \
-H "Content-Type: application/json" \
--data-binary @- <<DATA
{
  "id": 2,
  "firstName": "Dejah",
  "lastName": "Thoris",
  "age": 44
}
DATA
