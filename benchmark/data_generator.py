
import random
from string import digits, ascii_uppercase

legals = digits + ascii_uppercase

def rand_string(length, char_set = legals):
  output = ''
  for _ in range(length):
    output += random.choice(char_set)
  return output

def rand_numbers():
  output = ''
  for _ in range(20):
    output += str(random.randint(-2000000, 2000000)) + " "
  return output

# Generate short Strings

# f = open('short_strings.txt', 'w')
# for _ in range(1000000):
#   s = rand_string( random.randint(1, 50) ) + "\n"
#   f.write(s)
# f.close()


f = open('short_strings_spaces.txt', 'w')
for _ in range(100000):
  s = ""
  for _ in range(10):
      s += rand_string( random.randint(1, 50) ) + " "
  s += '\n'
  f.write(s)
f.close()


# f = open('integers.txt', 'w')
# for _ in range(100000):
#   s = rand_numbers() + "\n"
#   f.write(s)
# f.close()

