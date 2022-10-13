 
listView('demo_antologic Jobs') {
    description('demo_antologic Jobs')
    jobs {
        regex('demo_antologic_.+')
    }
    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}
