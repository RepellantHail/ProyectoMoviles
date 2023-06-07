
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.Adaptador_Citas
import com.example.proyectomoviles.R
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlin.concurrent.fixedRateTimer



class Citas : Fragment() {

    //Estas variables almacenan datos de las citas
    lateinit var nombrePaciente: MutableList<String>
    lateinit var nombreDoctor: MutableList<String>
    lateinit var fecha: MutableList<String>

    //Estas variables son para poder implementar el recyclerView
    private lateinit var miRecyclerView: RecyclerView
    lateinit var adaptadorCitas: Adaptador_Citas
    lateinit var miLayoutManager: RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) //Esta funcion se utliza para poider habilidar el menu.


    }

    //Esta funcion es para poder crear el elemento de recyclerView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_citas, container, false)
        miRecyclerView = view.findViewById(R.id.recyclerView_Citas)

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nombrePaciente = getAllPacientes()
        nombreDoctor = getAllDoctores()
        fecha = getAllFechas()

        miLayoutManager = LinearLayoutManager(requireContext())
        miRecyclerView.layoutManager = miLayoutManager

        adaptadorCitas = Adaptador_Citas(nombrePaciente,nombreDoctor,fecha,R.layout.citas_recycler_view_item, object : Adaptador_Citas.OnItemClickListener {

            override fun onItemClick(nombrePaciente: String?, nombreDoctor: String?, fecha: String?, position: Int) {

                //eliminarCita(0)
            }
        })

        miRecyclerView.adapter = adaptadorCitas

        registerForContextMenu(miRecyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.activity_main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.agregar_cita->agregarCita(0)
            else->super.onOptionsItemSelected(item)
        }
    }


    override fun onContextItemSelected(item: MenuItem): Boolean {
        //Toast.makeText(this, item.title, Toast.LENGTH_SHORT).show()
        return when (item.itemId) {
            R.id.eliminar_cita->eliminarCita(0)
            else -> super.onContextItemSelected(item)
        }
    }

    private fun agregarCita(position: Int):Boolean{
        nombrePaciente.add(position, " ");
        nombreDoctor.add(position, " ");
        fecha.add(position, " ");
        adaptadorCitas.notifyItemInserted(position)
        miLayoutManager.scrollToPosition(position)
        return true
    }

    private fun eliminarCita(position: Int):Boolean{
        nombrePaciente.removeAt(position);
        nombreDoctor.removeAt(position);
        fecha.removeAt(position);
        adaptadorCitas.notifyItemRemoved(position)
        return true
    }

    private fun getAllPacientes():MutableList<String>{
        return object :ArrayList<String>(){
            init{
                add("Alejandro Camacho")
                add("José Rodriguez")
                add("Luis Fernandez")
                add("Rubén Oligueri")
                add("Antonio Cardenas")
                add("Ricardo Gutierrez")
            }
        }
    }

    private fun getAllDoctores():MutableList<String>{
        return object :ArrayList<String>(){
            init{
                add("Luis Escobar")
                add("Pedro Rodriguez")
                add("Manuel Perez")
                add("Cesar Ontiveros")
                add("Frida Palomino")
                add("Ana Aguiñez")
            }
        }
    }

    private fun getAllFechas():MutableList<String>{
        return object :ArrayList<String>(){
            init{
                add("25/Agosto/2023")
                add("2/Agosto/2023")
                add("10/Agosto/2023")
                add("9/Agosto/2023")
                add("25/Septiembre/2023")
                add("5/Julio/2023")
            }
        }
    }
}