from abc import ABC, abstractmethod
class ActorFactory(ABC):
    @abstractmethod
    def createActor():
        pass

class Actor(ABC):

    @abstractmethod
    def nextLine():
        pass

    @abstractmethod
    def deliver(receptor):
        
        pass

    @abstractmethd
    def createActor():
        pass

class Player(Actor):
    pass

class NPC(Actor):
    pass

