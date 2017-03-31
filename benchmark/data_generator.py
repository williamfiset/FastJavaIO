
import random
from string import digits, ascii_uppercase

legals = digits + ascii_uppercase

def rand_string(length, char_set = legals):
  output = ''
  for _ in range(length):
    output += random.choice(char_set)
  return output

def rand_ints(ints_per_line = 20):
  output = ''
  for _ in range(ints_per_line):
    output += str(random.randint(-2000000, 2000000)) + " "
  return output

def rand_doubles(doubles_per_line = 20):
    output = ''
    for _ in range(doubles_per_line):
        num   = random.randint(-20000000, 20000000)
        denom = random.randint(-20000000, 20000000)
        if denom != 0:
            
            frac = round(float(num) / denom, 4)
            
            #frac *= 1000000
            #frac = int(frac)
            #frac /= 1000000.0

            output += str(frac) + " "
    return output

# Generate short Strings
def gen_short_string_file():

    #f = open('short_strings.txt', 'w')
    #for _ in range(1000000):
    #  s = rand_string( random.randint(1, 50) ) + "\n"
    #  f.write(s)
    #f.close()

    f = open('short_strings_spaces.txt', 'w')
    for _ in range(100000):
      s = ""
      for _ in range(10):
          s += rand_string( random.randint(1, 50) ) + " "
      s += '\n'
      f.write(s)
    f.close()

def gen_ints_file():

    f = open('integers.txt', 'w')
    for _ in range(100000):
        s = rand_ints() + "\n"
        f.write(s)
    f.close()

def gen_doubles_file(fname = 'doubles.txt', lines = 100000):

    f = open(fname, 'w')
    for _ in range(lines):
        s = rand_doubles() + "\n"
        f.write(s)
    f.close()

gen_doubles_file('doubles_small.txt', lines = 10)
gen_doubles_file()





