self.addEventListener('message', (message) => {
    eval(message.data);
});