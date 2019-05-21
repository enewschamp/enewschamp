
#Create users
newman run collections/eNewsChamp-Users.postman_collection.json --folder Users  -e eNewsChamp.postman_environment.json -d data/Users.json

#Create user roles
newman run collections/eNewsChamp-Users.postman_collection.json --folder Roles  -e eNewsChamp.postman_environment.json -d data/UserRoles.json

# Create genres
newman run collections/eNewsChamp-Genre.postman_collection.json  -e eNewsChamp.postman_environment.json -d data/Genres.json

# Create editions
newman run collections/eNewsChamp-Edition.postman_collection.json  -e eNewsChamp.postman_environment.json -d data/Editions.json

# Create articles
newman run collections/eNewsChamp-Article.postman_collection.json  -e eNewsChamp.postman_environment.json -d data/Articles.json