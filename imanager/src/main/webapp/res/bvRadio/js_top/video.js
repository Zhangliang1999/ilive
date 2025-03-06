


   $("#date1").click(function () {
            var appdt = $("input[name='appDT']").val();
            var datearr = {};
            datearr = appdt.split(' ');
            if (datearr[0] == datearr[2]) {
                alert(datearr[0]);
                $("input[name='appDT']").val('')
            }
        });