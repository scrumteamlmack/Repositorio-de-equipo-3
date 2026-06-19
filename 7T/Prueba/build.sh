#!/usr/bin/env bash
# Exit on error
set -o errexit

# Install dependencies
pip install -r requirements.txt

# Compile static files
python manage.py collectstatic --no-input

# Create helper temporary table if missing to prevent migration crash
python create_temp_table.py

# Run migrations faking initials
python manage.py migrate --fake-initial
