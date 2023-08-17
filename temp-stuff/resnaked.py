#!/usr/bin/env python3

# stty -echo
# trap 'end 130' EXIT INT; trap init_term WINCH
# 
# skip(){ if (( ${n:=1} > ${1:-1} )); then :; else ((n++)); false; fi; }
def skip():
    # n global scope. don't know it's purpose yet.
    n = "temporary"
    if n is None: # aka is null
        n = 1
    # don't know ${1:1} meaning


# 
# init_term(){
#   IFS='[;' read -sp $'\e[999;999H\e[6n' -d R -rs _ LINES COLS
#   ((ROWS=LINES-1)); ((snakeX=COLS/2)); ((snakeY=ROWS/2)); CENTER="$snakeY;$snakeX"
# 
#   printf '\e[?1049h\e[2J\e[?25l\e[?7l'
#   rand_block
# }
# 
# end(){ stty echo; printf '\e[?1049l\e[?25h\e[?7h'; exit "${1:-0}"; }

# game_over(){
#   local i // what value does it store???
#   until (( i > $1 )); do // if i unassigned, then why this condition?
#     for _ in 1 7; do
#       printf '\e[%sH\e[3D\e[3%dmGAME OVER\e[m' "$CENTER" "$_"
#       sleep .1
#     done; ((++i))
#   done; end "$2"
# }
def game_over(par1, par2):
    i: int
    while not (i > par1):
        # _ in bash is name of script. "snake"
        print('\e[{0}H\e[3D\e[3{1}mGAME OVER\e[m'.format("$CENTER","$_")) # probably should replace center & $_
        sleep .1
        

# read_keys(){
#   local i
#   case $1 in
#     20) i=.7;; 19) i=.6;; 18) i=.5;; 17) i=.4;; 16) i=.3;; 15) i=.2;; 14) i=.1;;
#     13) i=.09;; 12) i=.08;; 11) i=.075;; 10) i=.07;; 9) i=.065;; 8) i=.06;; 7) i=.055;;
#     6) i=.05;; 5) i=.045;; 4) i=.04;; 3) i=.035;; 2) i=.03;; 1) i=.025;;
#   esac
# 
#   while read -rsn1 -t "$i"; do
#     [[ $REPLY == $'\e' ]]&& read -rsn2 -t "$i"; KEY="${REPLY^^}"
#   done
# }
def read_keys(par1):
    # local var i
    i: float
    if par1 in range(14,21):
        i = (par1 - 13) * .1
    elif par1 in range(1, 14):
        i = par1 * .005 + 0.02
    #
    # while read key -t "$i".....
