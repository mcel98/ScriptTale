class MessageHandler:

    def __init__(self, _CommandPool):
        self.CommandPool = _CommandPool

    def add(Command):
        self.CommandPool.push(Command)

    def fetch():
        return self.CommandPool.pop(Command)

    def executeCommand():
        try:
            command = fetch()
            command.execute()           
        except:
            print("Command Pool Empty")



