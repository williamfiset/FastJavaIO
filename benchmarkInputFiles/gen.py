# Generate random input files for different data types
from random import randint
import string, random

print("Generating integers.txt")
integer_file = open('integers.txt', 'w')
for _ in range(5000000):
  integer_file.write( str(randint(-100000, 100000)) + "\n" )
integer_file.close()

print("Generating strings.txt")
strings_file = open('strings.txt', 'w')
random_chars = "+_)(*&^%$#@!)" + string.ascii_uppercase + string.digits
line_len = 1000
for _ in range(50000):
  rand_str = ''.join(random.choice(random_chars) for _ in range(line_len))
  strings_file.write( rand_str +  "\n" )
strings_file.close()

print("Generating doubles.txt")
doubles_file = open('doubles.txt', 'w')
sgn = 1
for _ in range(5000000):
  double_val = randint(0, 100000) + float(sgn*randint(1223523, 346346547)) / randint(1223523, 346346547)
  doubles_file.write( str( double_val ) + "\n" )
  sgn = -sgn
doubles_file.close()


