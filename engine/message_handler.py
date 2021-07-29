import queue

class MessageHandler:

    def __init__(self, _CommandPool):
        self.CommandPool = _CommandPool
        self.Channel

    def add(Command):
        self.CommandPool.push(Command)

    def fetch():
        return self.CommandPool.pop(Command)

    def executeCommand():
        try:
            command = fetch()
                      
        except:
            print("Command Pool Empty")

    def MessageRound():
        while not CommandPool.empty():
            executeCommand()



