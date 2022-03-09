1. Форк KotlinAsFirst2020 через сайт github
2. Terminal: git remote add upstream-my https://github.com/kirsanovdi/KotlinAsFirst2021
             git fetch upstream-my
3. Terminal: git branch backport 
   checkout backport через gui intellij idea
   Remote/upstream-my/master -> выбрать Rebase current onto Selected
4. Terminal: git remote add upstream-theirs https://github.com/Nikanorenkov-Mihail/KotlinAsFirst2021
             git fetch upstream-theirs
5. checkout Local/master через gui intellij idea
   Terminal: git merge upstream-my/master -Xtheirs
             git merge upstream-theirs/master -Xours
6. Terminal: git remote -v -> вывод в файл remotes.txt