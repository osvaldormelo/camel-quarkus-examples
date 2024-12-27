import json
import avro.schema
import avro.io
import avro.datafile

# Define the Avro schema
schema_dict = {
    "type": "record",
    "name": "User",
    "fields": [
        {"name": "name", "type": "string"},
        {"name": "age", "type": "int"},
        {"name": "email", "type": "string"}
    ]
}
schema = avro.schema.parse(json.dumps(schema_dict))

# Generate Avro data
data = [
    {"name": "John Doe", "age": 30, "email": "john.doe@example.com"},
    {"name": "Jane Smith", "age": 25, "email": "jane.smith@example.com"},
    {"name": "Bob Johnson", "age": 40, "email": "bob.johnson@example.com"}
]

# Write Avro file
avro_file_path = 'example.avro'
with open(avro_file_path, 'wb') as out_file:
    writer = avro.datafile.DataFileWriter(out_file, avro.io.DatumWriter(), schema)
    for record in data:
        writer.append(record)
    writer.close()

print(f"Avro file generated: {avro_file_path}")