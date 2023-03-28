if [ ! -z $(git rev-list ..main) ]
then
    echo "abandoning commit"
    echo "please merge from main and try again"
    exit 1
fi
