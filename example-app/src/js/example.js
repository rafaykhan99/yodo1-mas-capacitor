import { Yodo1Mas } from '@yodo1/mas-capacitor';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    Yodo1Mas.echo({ value: inputValue })
}
