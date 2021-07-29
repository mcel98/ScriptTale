class casa:
    def damealgo(cartero):
        cartero.generico()


class cartero:
    #atributos: f,sender,casa
    def __init__(self, fcallback,_sender, _casa):
        self.sender = _sender 
        self.casa = _casa
        self.f = fcallback

    def generico(self):
       print('generico')

    def messagepassing(self):
        casa.damealgo(self)


def f():
    print(x)
    pass
casaGenerica = casa()
casaDePepito = casa()
CarteroJuan = cartero(f,casaGenerica,casaDePepito) # -> dame un espacio de memoria para crear un cartero
                                                # se guarda en su atributo sender a casaGenerica
                                                # y se guarda la casa de pepito
                                                # en su atributo casa                                                
CarteroJuan.messagepassing()

    
