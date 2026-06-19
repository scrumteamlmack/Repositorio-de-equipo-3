import os
import pymysql

# Cargar credenciales desde variables de entorno con fallback a tus datos públicos
host = os.environ.get('MYSQLHOST', 'thomas.proxy.rlwy.net')
try:
    port = int(os.environ.get('MYSQLPORT', 27059))
except (ValueError, TypeError):
    port = 27059
user = os.environ.get('MYSQLUSER', 'root')
password = os.environ.get('MYSQLPASSWORD', 'vJNxCtfkHJNYpqlXnwjtSROAaGVriQgc')
db = os.environ.get('MYSQLDATABASE', 'railway')

def create_table():
    try:
        print(f"[Helper] Conectando a la base de datos MySQL en {host}:{port}...")
        connection = pymysql.connect(
            host=host,
            port=port,
            user=user,
            password=password,
            database=db
        )
        with connection.cursor() as cursor:
            print("[Helper] Creando tabla temporal 'ficha_instructor' si no existe...")
            cursor.execute("CREATE TABLE IF NOT EXISTS ficha_instructor (id INT AUTO_INCREMENT PRIMARY KEY)")
            connection.commit()
            print("[Helper] Tabla 'ficha_instructor' lista para el proceso de migración.")
        connection.close()
    except Exception as e:
        print(f"[Helper] Advertencia/Error al intentar crear la tabla temporal: {e}")

if __name__ == '__main__':
    create_table()
