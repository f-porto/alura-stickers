# Movie Sticker Maker

Até o momento esse projeto só faz pegar o *top* 250 filmes ou séries do IMDb e exibí-los no terminal ou criar stickers,
mas ainda falta deixar a criação de stickers mais personalizável.

Um exemplo do que ele pode fazer:

![stickerzinho](./images/tt0111161.png "stickerzinho")

Como você pode ver tem muito o que ser trabalhado.

## Aviso

Se você tentar rodar o projeto, muito provavelmente ele não vai funcionar como esperado, já que ele tentará pegar a
chave da api por um arquivo `.env`, se esse arquivo não existir ele vai pegar os dados de outro *site* que simula o que
IMDb faria, caso você queira usar o IMDb com a sua chave, você terá que criar um arquivo `.env` no diretório `src`.

<html lang="pt-br">
── Raiz <br>
&nbsp&nbsp&nbsp&nbsp └─ src <br>
&nbsp&nbsp&nbsp&nbsp &nbsp&nbsp&nbsp&nbsp └─ .env
</html>

Esse arquivo deve apenas ter a chave da api e mais nada.
