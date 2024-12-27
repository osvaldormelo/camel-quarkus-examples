import pandas as pd
import pyarrow as pa
import pyarrow.parquet as pq

# Generate data
data = [
    {"name": "John Doe", "age": 30, "email": "john.doe@example.com"},
    {"name": "Jane Smith", "age": 25, "email": "jane.smith@example.com"},
    {"name": "Bob Johnson", "age": 40, "email": "bob.johnson@example.com"}
]

# Convert data to DataFrame
df = pd.DataFrame(data)

# Write Parquet file
parquet_file_path = 'example.parquet'
table = pa.Table.from_pandas(df)
pq.write_table(table, parquet_file_path)

print(f"Parquet file generated: {parquet_file_path}")